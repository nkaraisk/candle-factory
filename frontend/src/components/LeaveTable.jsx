import { useState } from 'react';

const toDays = (start, end) => {
  if (!start || !end) return 0;
  const startDate = new Date(`${start}T00:00:00`);
  const endDate = new Date(`${end}T00:00:00`);
  const diff = (endDate - startDate) / (1000 * 60 * 60 * 24);
  return Math.round(diff) + 1;
};

const formatDate = (date) => new Date(`${date}T00:00:00`).toLocaleDateString();

export default function LeaveTable({ leaves, onDelete, onEdit, loading }) {
  const [editingId, setEditingId] = useState(null);
  const [draft, setDraft] = useState({ startDate: '', endDate: '' });

  const startEdit = (leave) => {
    setEditingId(leave.id);
    setDraft({ startDate: leave.startDate, endDate: leave.endDate, workerId: leave.worker?.id });
  };

  const saveEdit = async () => {
    if (!draft.startDate || !draft.endDate) return;
    await onEdit({ ...draft, leaveId: editingId });
    setEditingId(null);
  };

  const cancel = () => {
    setEditingId(null);
    setDraft({ startDate: '', endDate: '' });
  };

  if (!leaves.length) {
    return <p className="muted">No leaves to show for this worker yet.</p>;
  }

  return (
    <div className="table-shell compact">
      <div className="table-row table-row--header">
        <span>Period</span>
        <span>Days</span>
        <span>Worker</span>
        <span className="tight">Actions</span>
      </div>
      {leaves.map((leave) => {
        const isEditing = editingId === leave.id;
        const workerName = leave.worker ? `${leave.worker.firstName} ${leave.worker.lastName}` : '—';

        return (
          <div key={leave.id} className={`table-row ${isEditing ? 'is-editing' : ''}`}>
            <span>
              {isEditing ? (
                <div className="edit-dates">
                  <input
                    type="date"
                    value={draft.startDate}
                    onChange={(e) => setDraft((prev) => ({ ...prev, startDate: e.target.value }))}
                    disabled={loading}
                  />
                  <input
                    type="date"
                    value={draft.endDate}
                    onChange={(e) => setDraft((prev) => ({ ...prev, endDate: e.target.value }))}
                    disabled={loading}
                  />
                </div>
              ) : (
                <div className="period">
                  <span>{formatDate(leave.startDate)}</span>
                  <span className="dash">→</span>
                  <span>{formatDate(leave.endDate)}</span>
                </div>
              )}
            </span>
            <span className="mono">{toDays(leave.startDate, leave.endDate)} days</span>
            <span>{workerName}</span>
            <span className="actions tight">
              {isEditing ? (
                <>
                  <button className="primary" onClick={saveEdit} disabled={loading}>
                    Save
                  </button>
                  <button className="ghost" onClick={cancel} disabled={loading}>
                    Cancel
                  </button>
                </>
              ) : (
                <>
                  <button className="ghost" onClick={() => startEdit(leave)} disabled={loading}>
                    Edit
                  </button>
                  <button className="ghost danger" onClick={() => onDelete(leave.id)} disabled={loading}>
                    Delete
                  </button>
                </>
              )}
            </span>
          </div>
        );
      })}
    </div>
  );
}
