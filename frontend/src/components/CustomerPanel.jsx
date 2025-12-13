import { useMemo, useState } from 'react';

const empty = { customerName: '', customerPhone: '' };

const formatCurrency = (value) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'EUR' }).format(Number(value || 0));

export default function CustomerPanel({ customers, onAdd, onEdit, onDelete, loading }) {
  const [form, setForm] = useState(empty);
  const [editingId, setEditingId] = useState(null);
  const [draft, setDraft] = useState(empty);

  const sortedCustomers = useMemo(
    () => [...customers].sort((a, b) => a.name.localeCompare(b.name)),
    [customers],
  );

  const handleAdd = async (e) => {
    e.preventDefault();
    if (!form.customerName.trim() || !form.customerPhone.trim()) return;
    await onAdd(form);
    setForm(empty);
  };

  const startEdit = (customer) => {
    setEditingId(customer.id);
    setDraft({ customerName: customer.name, customerPhone: customer.phoneNumber });
  };

  const saveEdit = async () => {
    if (!draft.customerName.trim() || !draft.customerPhone.trim()) return;
    await onEdit({ customerId: editingId, ...draft });
    setEditingId(null);
  };

  const cancel = () => {
    setEditingId(null);
    setDraft(empty);
  };

  return (
    <div className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Customers</p>
          <h2>Accounts</h2>
        </div>
        <span className="pill ghost">{customers.length} total</span>
      </div>

      <form className="panel-form" onSubmit={handleAdd}>
        <div className="fields">
          <label className="field">
            <span>Name</span>
            <input
              value={form.customerName}
              onChange={(e) => setForm((prev) => ({ ...prev, customerName: e.target.value }))}
              placeholder="Retailer, etc."
              disabled={loading}
            />
          </label>
          <label className="field">
            <span>Phone</span>
            <input
              value={form.customerPhone}
              onChange={(e) => setForm((prev) => ({ ...prev, customerPhone: e.target.value }))}
              placeholder="+30..."
              disabled={loading}
            />
          </label>
        </div>
        <div className="actions">
          <button className="primary" type="submit" disabled={loading}>
            Add customer
          </button>
        </div>
      </form>

      <div className="panel-inner">
        <div className="table-row table-row--header">
          <span>Name</span>
          <span>Phone</span>
          <span>Debt</span>
          <span className="tight">Actions</span>
        </div>
        {sortedCustomers.map((customer) => {
          const isEditing = editingId === customer.id;
          return (
            <div key={customer.id} className={`table-row ${isEditing ? 'is-editing' : ''}`}>
              <span>
                {isEditing ? (
                  <input
                    value={draft.customerName}
                    onChange={(e) => setDraft((prev) => ({ ...prev, customerName: e.target.value }))}
                    disabled={loading}
                  />
                ) : (
                  <strong>{customer.name}</strong>
                )}
              </span>
              <span>
                {isEditing ? (
                  <input
                    value={draft.customerPhone}
                    onChange={(e) => setDraft((prev) => ({ ...prev, customerPhone: e.target.value }))}
                    disabled={loading}
                  />
                ) : (
                  <span className="mono">{customer.phoneNumber}</span>
                )}
              </span>
              <span className="mono">{formatCurrency(customer.debt)}</span>
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
                    <button className="ghost" onClick={() => startEdit(customer)} disabled={loading}>
                      Edit
                    </button>
                    <button
                      className="ghost danger"
                      onClick={() => onDelete(customer.id)}
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
        {!sortedCustomers.length ? <p className="muted">No customers yet.</p> : null}
      </div>
    </div>
  );
}
