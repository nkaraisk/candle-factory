import { useMemo, useState } from 'react';

export default function WorkerList({
  workers,
  selectedWorkerId,
  onSelect,
  onDelete,
  onEdit,
  loading,
}) {
  const [editingId, setEditingId] = useState(null);
  const [draft, setDraft] = useState({ firstName: '', lastName: '', phoneNumber: '' });

  const sortedWorkers = useMemo(
    () => [...workers].sort((a, b) => a.firstName.localeCompare(b.firstName)),
    [workers],
  );

  const startEdit = (worker) => {
    setEditingId(worker.id);
    setDraft({
      firstName: worker.firstName,
      lastName: worker.lastName,
      phoneNumber: worker.phoneNumber,
    });
  };

  const handleEdit = async () => {
    if (!draft.firstName.trim() || !draft.lastName.trim() || !draft.phoneNumber.trim()) {
      return;
    }
    await onEdit(editingId, draft);
    setEditingId(null);
  };

  const cancelEdit = () => {
    setEditingId(null);
    setDraft({ firstName: '', lastName: '', phoneNumber: '' });
  };

  return (
    <div className="table-shell">
      <div className="table-head">
        <div>
          <p className="eyebrow">Team</p>
          <h3>Workers ({workers.length})</h3>
        </div>
        <button className="ghost" onClick={() => onSelect(null)} disabled={loading}>
          Clear selection
        </button>
      </div>
      <div className="table">
        <div className="table-row table-row--header">
          <span>Worker</span>
          <span>Phone</span>
          <span>Used leave</span>
          <span className="tight">Actions</span>
        </div>
        {sortedWorkers.map((worker) => {
          const isEditing = editingId === worker.id;
          const isActive = selectedWorkerId === worker.id;

          return (
            <div
              key={worker.id}
              className={`table-row ${isActive ? 'is-active' : ''} ${isEditing ? 'is-editing' : ''}`}
            >
              <span>
                {isEditing ? (
                  <div className="edit-names">
                    <input
                      value={draft.firstName}
                      onChange={(e) => setDraft((prev) => ({ ...prev, firstName: e.target.value }))}
                      placeholder="First name"
                    />
                    <input
                      value={draft.lastName}
                      onChange={(e) => setDraft((prev) => ({ ...prev, lastName: e.target.value }))}
                      placeholder="Last name"
                    />
                  </div>
                ) : (
                  <button className="linkish" onClick={() => onSelect(worker.id)} disabled={loading}>
                    {worker.firstName} {worker.lastName}
                  </button>
                )}
              </span>
              <span>
                {isEditing ? (
                  <input
                    value={draft.phoneNumber}
                    onChange={(e) => setDraft((prev) => ({ ...prev, phoneNumber: e.target.value }))}
                  />
                ) : (
                  <span className="mono">{worker.phoneNumber}</span>
                )}
              </span>
              <span className="mono">{worker.daysOfLeave ?? 0} days</span>
              <span className="actions tight">
                {isEditing ? (
                  <>
                    <button className="primary" onClick={handleEdit} disabled={loading}>
                      Save
                    </button>
                    <button className="ghost" onClick={cancelEdit} disabled={loading}>
                      Cancel
                    </button>
                  </>
                ) : (
                  <>
                    <button className="ghost" onClick={() => startEdit(worker)} disabled={loading}>
                      Edit
                    </button>
                    <button
                      className="ghost danger"
                      onClick={() => onDelete(worker.id)}
                      disabled={loading}
                    >
                      Delete
                    </button>
                  </>
                )}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
}
