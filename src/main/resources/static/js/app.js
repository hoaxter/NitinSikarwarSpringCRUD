/*
 * Student Portal — Client-side Logic
 */

var ENDPOINT = '/students';

var records  = [];
var deleteId = null;

// Bootstrap
window.addEventListener('load', loadRecords);

// ──── Data fetching ──────────────────────────

async function loadRecords() {
    try {
        var resp = await fetch(ENDPOINT);
        if (!resp.ok) throw new Error(resp.statusText);
        records = await resp.json();
        paint(records);
        refreshMetrics(records);
        markServer(true);
    } catch (e) {
        console.error('Load failed:', e);
        markServer(false);
        notify('Could not reach the server.', false);
        paint([]);
        refreshMetrics([]);
    }
}

async function saveRecord(payload) {
    try {
        var resp = await fetch(ENDPOINT, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!resp.ok) throw new Error(resp.statusText);
        var result = await resp.json();
        notify(result.name + ' has been added.');
        loadRecords();
        return true;
    } catch (e) {
        console.error('Save failed:', e);
        notify('Could not save the record.', false);
        return false;
    }
}

async function patchRecord(id, payload) {
    try {
        var resp = await fetch(ENDPOINT + '/' + id, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!resp.ok) throw new Error(resp.statusText);
        var result = await resp.json();
        notify(result.name + ' has been updated.');
        loadRecords();
        return true;
    } catch (e) {
        console.error('Patch failed:', e);
        notify('Could not update the record.', false);
        return false;
    }
}

async function destroyRecord(id) {
    try {
        var resp = await fetch(ENDPOINT + '/' + id, { method: 'DELETE' });
        if (!resp.ok) throw new Error(resp.statusText);
        notify('Record removed successfully.');
        loadRecords();
    } catch (e) {
        console.error('Delete failed:', e);
        notify('Could not remove the record.', false);
    }
}

// ──── Rendering ──────────────────────────────

function paint(list) {
    var body        = document.getElementById('gridBody');
    var placeholder = document.getElementById('placeholder');
    var wrap        = document.getElementById('gridWrap');
    var badge       = document.getElementById('rowBadge');

    badge.textContent = list.length;

    if (list.length === 0) {
        body.innerHTML = '';
        wrap.style.display = 'none';
        placeholder.style.display = 'block';
        return;
    }

    wrap.style.display = 'block';
    placeholder.style.display = 'none';

    body.innerHTML = list.map(function (s, idx) {
        return '<tr style="animation-delay:' + (idx * 35) + 'ms">' +
            '<td>' + s.id + '</td>' +
            '<td class="name-cell">' + safe(s.name) + '</td>' +
            '<td class="email-cell">' + safe(s.email) + '</td>' +
            '<td><span class="course-tag">' + safe(s.course) + '</span></td>' +
            '<td><div class="opts-cell">' +
                '<button class="row-btn row-edit" title="Edit" onclick="openEdit(' + s.id + ')">' +
                    '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M17 3a2.828 2.828 0 114 4L7.5 20.5 2 22l1.5-5.5L17 3z"/></svg>' +
                '</button>' +
                '<button class="row-btn row-del" title="Delete" onclick="openConfirm(' + s.id + ',\'' + safe(s.name) + '\')">' +
                    '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 01-2 2H8a2 2 0 01-2-2L5 6"/><path d="M10 11v6"/><path d="M14 11v6"/><path d="M9 6V4a1 1 0 011-1h4a1 1 0 011 1v2"/></svg>' +
                '</button>' +
            '</div></td>' +
        '</tr>';
    }).join('');
}

function refreshMetrics(list) {
    document.getElementById('metricTotal').textContent = list.length;
    var unique = {};
    list.forEach(function (s) { unique[s.course] = true; });
    document.getElementById('metricCourses').textContent = Object.keys(unique).length;
}

function markServer(up) {
    var el = document.getElementById('metricStatus');
    el.textContent = up ? 'Active' : 'Down';
    el.style.color = up ? '' : '#ef4444';
    if (!up) el.classList.remove('summary-live');
    else     el.classList.add('summary-live');
}

// ──── Filter ─────────────────────────────────

function applyFilter() {
    var q = document.getElementById('filterInput').value.toLowerCase();
    if (!q) { paint(records); return; }
    var hits = records.filter(function (s) {
        return s.name.toLowerCase().indexOf(q) > -1 ||
               s.email.toLowerCase().indexOf(q) > -1 ||
               s.course.toLowerCase().indexOf(q) > -1;
    });
    paint(hits);
}

// ──── Form drawer ────────────────────────────

function showForm() {
    document.getElementById('drawerTitle').textContent = 'New Student';
    document.getElementById('saveBtnLabel').textContent = 'Save';
    document.getElementById('entryForm').reset();
    document.getElementById('entryId').value = '';
    document.getElementById('overlay').classList.add('open');
    setTimeout(function () { document.getElementById('entryName').focus(); }, 280);
}

function openEdit(id) {
    var rec = records.find(function (s) { return s.id === id; });
    if (!rec) return;
    document.getElementById('drawerTitle').textContent = 'Edit Student';
    document.getElementById('saveBtnLabel').textContent = 'Update';
    document.getElementById('entryId').value    = rec.id;
    document.getElementById('entryName').value  = rec.name;
    document.getElementById('entryEmail').value = rec.email;
    document.getElementById('entryCourse').value = rec.course;
    document.getElementById('overlay').classList.add('open');
    setTimeout(function () { document.getElementById('entryName').focus(); }, 280);
}

function hideForm(ev) {
    if (ev && ev.target !== document.getElementById('overlay')) return;
    document.getElementById('overlay').classList.remove('open');
}

async function submitEntry(ev) {
    ev.preventDefault();
    var id = document.getElementById('entryId').value;
    var payload = {
        name:   document.getElementById('entryName').value.trim(),
        email:  document.getElementById('entryEmail').value.trim(),
        course: document.getElementById('entryCourse').value.trim()
    };
    var ok = id ? await patchRecord(id, payload) : await saveRecord(payload);
    if (ok) hideForm();
}

// ──── Delete confirm ─────────────────────────

function openConfirm(id, name) {
    deleteId = id;
    document.getElementById('confirmName').textContent = name;
    document.getElementById('confirmOverlay').classList.add('open');
}

function hideConfirm(ev) {
    if (ev && ev.target !== document.getElementById('confirmOverlay')) return;
    document.getElementById('confirmOverlay').classList.remove('open');
    deleteId = null;
}

async function performDelete() {
    if (deleteId === null) return;
    await destroyRecord(deleteId);
    hideConfirm();
}

// ──── Toast ──────────────────────────────────

function notify(msg, ok) {
    if (ok === undefined) ok = true;
    var area = document.getElementById('toastArea');
    var el   = document.createElement('div');
    el.className = 'toast ' + (ok ? 'toast--ok' : 'toast--err');
    el.textContent = msg;
    area.appendChild(el);
    setTimeout(function () {
        el.classList.add('out');
        el.addEventListener('animationend', function () { el.remove(); });
    }, 2800);
}

// ──── Helpers ────────────────────────────────

function safe(str) {
    var node = document.createElement('span');
    node.textContent = str;
    return node.innerHTML;
}

document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') { hideForm(); hideConfirm(); }
});
