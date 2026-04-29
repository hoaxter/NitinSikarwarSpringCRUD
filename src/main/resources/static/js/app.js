/* =============================================
   Student Manager — Frontend Logic
   ============================================= */

const API_BASE = '/students';

let allStudents = [];
let deleteTargetId = null;

// ─── Init ────────────────────────────────────

document.addEventListener('DOMContentLoaded', () => {
    fetchStudents();
});

// ─── CRUD Operations ─────────────────────────

async function fetchStudents() {
    showLoadingSkeleton();
    try {
        const res = await fetch(API_BASE);
        if (!res.ok) throw new Error('Failed to fetch students');
        allStudents = await res.json();
        renderStudents(allStudents);
        updateStats(allStudents);
        setApiStatus(true);
    } catch (err) {
        console.error(err);
        setApiStatus(false);
        showToast('Failed to load students. Is the server running?', 'error');
        renderStudents([]);
        updateStats([]);
    }
}

async function createStudent(data) {
    try {
        const res = await fetch(API_BASE, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!res.ok) throw new Error('Failed to create student');
        const student = await res.json();
        showToast(`${student.name} added successfully!`, 'success');
        fetchStudents();
        return true;
    } catch (err) {
        console.error(err);
        showToast('Failed to create student.', 'error');
        return false;
    }
}

async function updateStudent(id, data) {
    try {
        const res = await fetch(`${API_BASE}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!res.ok) throw new Error('Failed to update student');
        const student = await res.json();
        showToast(`${student.name} updated successfully!`, 'success');
        fetchStudents();
        return true;
    } catch (err) {
        console.error(err);
        showToast('Failed to update student.', 'error');
        return false;
    }
}

async function deleteStudent(id) {
    try {
        const res = await fetch(`${API_BASE}/${id}`, {
            method: 'DELETE'
        });
        if (!res.ok) throw new Error('Failed to delete student');
        showToast('Student deleted successfully.', 'success');
        fetchStudents();
        return true;
    } catch (err) {
        console.error(err);
        showToast('Failed to delete student.', 'error');
        return false;
    }
}

// ─── Rendering ───────────────────────────────

function renderStudents(students) {
    const tbody = document.getElementById('studentTableBody');
    const emptyState = document.getElementById('emptyState');
    const tableWrapper = document.querySelector('.table-wrapper');
    const countEl = document.getElementById('recordCount');

    if (students.length === 0) {
        tbody.innerHTML = '';
        tableWrapper.style.display = 'none';
        emptyState.style.display = 'block';
        countEl.textContent = '0 records';
        return;
    }

    tableWrapper.style.display = 'block';
    emptyState.style.display = 'none';
    countEl.textContent = `${students.length} record${students.length !== 1 ? 's' : ''}`;

    tbody.innerHTML = students.map((s, i) => `
        <tr style="animation-delay: ${i * 0.04}s">
            <td>#${s.id}</td>
            <td><span class="student-name">${escapeHtml(s.name)}</span></td>
            <td><span class="student-email">${escapeHtml(s.email)}</span></td>
            <td><span class="course-badge">${escapeHtml(s.course)}</span></td>
            <td>
                <div class="actions-cell">
                    <button class="btn-icon edit" title="Edit" onclick="openEditModal(${s.id})">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                    </button>
                    <button class="btn-icon delete" title="Delete" onclick="openDeleteModal(${s.id}, '${escapeHtml(s.name)}')">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function showLoadingSkeleton() {
    const tbody = document.getElementById('studentTableBody');
    const rows = Array.from({ length: 4 }, () => `
        <tr class="skeleton-row">
            <td><span class="skeleton" style="width:30px">&nbsp;</span></td>
            <td><span class="skeleton" style="width:120px">&nbsp;</span></td>
            <td><span class="skeleton" style="width:160px">&nbsp;</span></td>
            <td><span class="skeleton" style="width:100px">&nbsp;</span></td>
            <td><span class="skeleton" style="width:70px">&nbsp;</span></td>
        </tr>
    `).join('');
    tbody.innerHTML = rows;
}

function updateStats(students) {
    document.getElementById('totalStudents').textContent = students.length;
    const courses = new Set(students.map(s => s.course));
    document.getElementById('totalCourses').textContent = courses.size;
}

function setApiStatus(online) {
    const el = document.getElementById('apiStatus');
    if (online) {
        el.textContent = 'Online';
        el.classList.add('stat-online');
    } else {
        el.textContent = 'Offline';
        el.classList.remove('stat-online');
        el.style.color = '#ef4444';
    }
}

// ─── Search / Filter ─────────────────────────

function filterStudents() {
    const query = document.getElementById('searchInput').value.toLowerCase().trim();
    if (!query) {
        renderStudents(allStudents);
        return;
    }
    const filtered = allStudents.filter(s =>
        s.name.toLowerCase().includes(query) ||
        s.email.toLowerCase().includes(query) ||
        s.course.toLowerCase().includes(query)
    );
    renderStudents(filtered);
}

// ─── Add / Edit Modal ────────────────────────

function openModal() {
    document.getElementById('modalTitle').textContent = 'Add New Student';
    document.getElementById('submitBtnText').textContent = 'Add Student';
    document.getElementById('studentForm').reset();
    document.getElementById('studentId').value = '';
    document.getElementById('modalOverlay').classList.add('active');
    setTimeout(() => document.getElementById('studentName').focus(), 300);
}

function openEditModal(id) {
    const student = allStudents.find(s => s.id === id);
    if (!student) return;

    document.getElementById('modalTitle').textContent = 'Edit Student';
    document.getElementById('submitBtnText').textContent = 'Save Changes';
    document.getElementById('studentId').value = student.id;
    document.getElementById('studentName').value = student.name;
    document.getElementById('studentEmail').value = student.email;
    document.getElementById('studentCourse').value = student.course;
    document.getElementById('modalOverlay').classList.add('active');
    setTimeout(() => document.getElementById('studentName').focus(), 300);
}

function closeModal(event) {
    if (event && event.target !== document.getElementById('modalOverlay')) return;
    document.getElementById('modalOverlay').classList.remove('active');
}

async function handleSubmit(event) {
    event.preventDefault();

    const id = document.getElementById('studentId').value;
    const data = {
        name: document.getElementById('studentName').value.trim(),
        email: document.getElementById('studentEmail').value.trim(),
        course: document.getElementById('studentCourse').value.trim()
    };

    let success;
    if (id) {
        success = await updateStudent(id, data);
    } else {
        success = await createStudent(data);
    }

    if (success) {
        closeModal();
    }
}

// ─── Delete Modal ────────────────────────────

function openDeleteModal(id, name) {
    deleteTargetId = id;
    document.getElementById('deleteStudentName').textContent = name;
    document.getElementById('deleteModalOverlay').classList.add('active');
}

function closeDeleteModal(event) {
    if (event && event.target !== document.getElementById('deleteModalOverlay')) return;
    document.getElementById('deleteModalOverlay').classList.remove('active');
    deleteTargetId = null;
}

async function confirmDelete() {
    if (deleteTargetId === null) return;
    await deleteStudent(deleteTargetId);
    closeDeleteModal();
}

// ─── Toast Notifications ─────────────────────

function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');

    const iconSvg = type === 'success'
        ? '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round"><polyline points="20 6 9 17 4 12"/></svg>'
        : '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>';

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
        <div class="toast-icon">${iconSvg}</div>
        <span class="toast-message">${escapeHtml(message)}</span>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('removing');
        toast.addEventListener('animationend', () => toast.remove());
    }, 3000);
}

// ─── Utilities ───────────────────────────────

function escapeHtml(str) {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

// Close modals on Escape key
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        closeModal();
        closeDeleteModal();
    }
});
