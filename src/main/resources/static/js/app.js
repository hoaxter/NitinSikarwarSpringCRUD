/**
 * EnrollHub — Client Module
 * Author: Nitin
 *
 * All logic is wrapped inside a single IIFE so nothing leaks
 * into the global scope except the NitinApp namespace.
 */
const NitinApp = (function () {
    'use strict';

    const API_BASE = '/api/enrollees';
    let enrollees = [];
    let pendingRemoveId = null;

    /* ──────── bootstrap ──────── */

    document.addEventListener('DOMContentLoaded', () => {
        refreshData();
    });

    document.addEventListener('keydown', (evt) => {
        if (evt.key === 'Escape') {
            closePanel();
            closeRemoveDialog();
        }
    });

    /* ──────── API calls ──────── */

    async function refreshData() {
        try {
            const res = await fetch(API_BASE);
            if (!res.ok) throw new Error(res.statusText);
            enrollees = await res.json();
            renderGrid(enrollees);
            computeKpis(enrollees);
            setHealth(true);
        } catch (err) {
            console.error('[EnrollHub] load error:', err);
            setHealth(false);
            flash('Unable to connect to the server.', false);
            renderGrid([]);
            computeKpis([]);
        }
    }

    async function createEnrollee(data) {
        try {
            const res = await fetch(API_BASE, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });
            if (!res.ok) throw new Error(res.statusText);
            const created = await res.json();
            flash(`${created.fullName} enrolled successfully.`);
            await refreshData();
            return true;
        } catch (err) {
            console.error('[EnrollHub] create error:', err);
            flash('Failed to create the enrollee.', false);
            return false;
        }
    }

    async function updateEnrollee(id, data) {
        try {
            const res = await fetch(`${API_BASE}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });
            if (!res.ok) throw new Error(res.statusText);
            const updated = await res.json();
            flash(`${updated.fullName} updated successfully.`);
            await refreshData();
            return true;
        } catch (err) {
            console.error('[EnrollHub] update error:', err);
            flash('Failed to update the enrollee.', false);
            return false;
        }
    }

    async function deleteEnrollee(id) {
        try {
            const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
            if (!res.ok) throw new Error(res.statusText);
            flash('Enrollee removed.');
            await refreshData();
        } catch (err) {
            console.error('[EnrollHub] delete error:', err);
            flash('Failed to remove the enrollee.', false);
        }
    }

    /* ──────── rendering ──────── */

    function renderGrid(list) {
        const container = document.getElementById('gridContainer');
        const blank = document.getElementById('blankState');
        const badge = document.getElementById('countBadge');

        badge.textContent = list.length;

        if (list.length === 0) {
            container.innerHTML = '';
            container.style.display = 'none';
            blank.style.display = 'block';
            return;
        }

        blank.style.display = 'none';
        container.style.display = 'grid';

        container.innerHTML = list.map((e, idx) => {
            const initials = e.fullName
                .split(' ')
                .map(w => w.charAt(0).toUpperCase())
                .slice(0, 2)
                .join('');

            return `
            <div class="enrollee-card" style="animation-delay:${idx * 40}ms">
                <div class="card-top">
                    <div class="card-avatar">${esc(initials)}</div>
                    <span class="card-id">#${e.enrolleeId}</span>
                </div>
                <div class="card-name">${esc(e.fullName)}</div>
                <div class="card-email">${esc(e.emailAddress)}</div>
                <span class="card-program">${esc(e.program)}</span>
                <div class="card-actions">
                    <button class="card-btn card-btn--edit" onclick="NitinApp.openEdit(${e.enrolleeId})">
                        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M17 3a2.828 2.828 0 114 4L7.5 20.5 2 22l1.5-5.5L17 3z"/></svg>
                        Edit
                    </button>
                    <button class="card-btn card-btn--remove" onclick="NitinApp.openRemoveDialog(${e.enrolleeId},'${esc(e.fullName)}')">
                        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 01-2 2H8a2 2 0 01-2-2L5 6"/><path d="M10 11v6"/><path d="M14 11v6"/></svg>
                        Remove
                    </button>
                </div>
            </div>`;
        }).join('');
    }

    function computeKpis(list) {
        document.getElementById('kpiTotal').textContent = list.length;
        const uniquePrograms = new Set(list.map(e => e.program));
        document.getElementById('kpiPrograms').textContent = uniquePrograms.size;
    }

    function setHealth(isUp) {
        const el = document.getElementById('kpiHealth');
        el.textContent = isUp ? 'Online' : 'Offline';
        if (isUp) {
            el.classList.add('metric-val--live');
            el.style.color = '';
        } else {
            el.classList.remove('metric-val--live');
            el.style.color = '#f87171';
        }
    }

    /* ──────── filter ──────── */

    function onFilter() {
        const query = document.getElementById('searchField').value.toLowerCase().trim();
        if (!query) {
            renderGrid(enrollees);
            return;
        }
        const matches = enrollees.filter(e =>
            e.fullName.toLowerCase().includes(query) ||
            e.emailAddress.toLowerCase().includes(query) ||
            e.program.toLowerCase().includes(query)
        );
        renderGrid(matches);
    }

    /* ──────── slide panel (create / edit) ──────── */

    function openPanel() {
        document.getElementById('panelHeading').textContent = 'New Enrollee';
        document.getElementById('saveLabel').textContent = 'Create';
        document.getElementById('enrollForm').reset();
        document.getElementById('hiddenId').value = '';
        document.getElementById('panelBackdrop').classList.add('visible');
        setTimeout(() => document.getElementById('fieldName').focus(), 300);
    }

    function openEdit(id) {
        const record = enrollees.find(e => e.enrolleeId === id);
        if (!record) return;
        document.getElementById('panelHeading').textContent = 'Edit Enrollee';
        document.getElementById('saveLabel').textContent = 'Save Changes';
        document.getElementById('hiddenId').value = record.enrolleeId;
        document.getElementById('fieldName').value = record.fullName;
        document.getElementById('fieldEmail').value = record.emailAddress;
        document.getElementById('fieldProgram').value = record.program;
        document.getElementById('panelBackdrop').classList.add('visible');
        setTimeout(() => document.getElementById('fieldName').focus(), 300);
    }

    function closePanel(evt) {
        if (evt && evt.target !== document.getElementById('panelBackdrop')) return;
        document.getElementById('panelBackdrop').classList.remove('visible');
    }

    async function handleSave(evt) {
        evt.preventDefault();
        const id = document.getElementById('hiddenId').value;
        const payload = {
            fullName: document.getElementById('fieldName').value.trim(),
            emailAddress: document.getElementById('fieldEmail').value.trim(),
            program: document.getElementById('fieldProgram').value.trim(),
        };
        const success = id
            ? await updateEnrollee(id, payload)
            : await createEnrollee(payload);
        if (success) closePanel();
    }

    /* ──────── remove dialog ──────── */

    function openRemoveDialog(id, name) {
        pendingRemoveId = id;
        document.getElementById('removeName').textContent = name;
        document.getElementById('removeBackdrop').classList.add('visible');
    }

    function closeRemoveDialog(evt) {
        if (evt && evt.target !== document.getElementById('removeBackdrop')) return;
        document.getElementById('removeBackdrop').classList.remove('visible');
        pendingRemoveId = null;
    }

    async function confirmRemove() {
        if (pendingRemoveId === null) return;
        await deleteEnrollee(pendingRemoveId);
        closeRemoveDialog();
    }

    /* ──────── notifications ──────── */

    function flash(msg, isOk = true) {
        const tray = document.getElementById('notifTray');
        const el = document.createElement('div');
        el.className = `notif ${isOk ? 'notif--success' : 'notif--error'}`;
        el.textContent = msg;
        tray.appendChild(el);
        setTimeout(() => {
            el.classList.add('fade-out');
            el.addEventListener('animationend', () => el.remove());
        }, 3000);
    }

    /* ──────── helpers ──────── */

    function esc(text) {
        const node = document.createElement('span');
        node.textContent = text;
        return node.innerHTML;
    }

    /* ──────── public API ──────── */

    return {
        openPanel,
        closePanel,
        openEdit,
        handleSave,
        openRemoveDialog,
        closeRemoveDialog,
        confirmRemove,
        onFilter,
    };

})();
