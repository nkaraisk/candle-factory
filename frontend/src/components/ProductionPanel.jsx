import { useMemo, useState } from 'react';

const initial = { date: '', productId: '', quantity: '' };

const formatDate = (date) => new Date(`${date}T00:00:00`).toLocaleDateString();

export default function ProductionPanel({ productions, products, onAdd, onEdit, onDelete, loading }) {
  const [form, setForm] = useState(initial);
  const [editingId, setEditingId] = useState(null);
  const [draft, setDraft] = useState(initial);

  const sortedProductions = useMemo(
    () =>
      [...productions].sort(
        (a, b) => new Date(b.dateOfProduction ?? b.date) - new Date(a.dateOfProduction ?? a.date),
      ),
    [productions],
  );

  const handleAdd = async (e) => {
    e.preventDefault();
    if (!form.date || !form.productId || !form.quantity) return;
    await onAdd({
      date: form.date,
      productId: Number(form.productId),
      quantity: Number(form.quantity),
    });
    setForm(initial);
  };

  const startEdit = (item) => {
    setEditingId(item.id);
    setDraft({
      date: item.dateOfProduction || item.date || '',
      productId: item.product?.id ?? '',
      quantity: item.quantity,
    });
  };

  const saveEdit = async () => {
    if (!draft.date || !draft.productId || !draft.quantity) return;
    await onEdit({
      id: editingId,
      date: draft.date,
      productId: Number(draft.productId),
      quantity: Number(draft.quantity),
    });
    setEditingId(null);
  };

  const cancel = () => {
    setEditingId(null);
    setDraft(initial);
  };

  return (
    <div className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Production</p>
          <h2>Build schedule</h2>
        </div>
        <span className="pill ghost">{productions.length} runs</span>
      </div>

      <form className="panel-form" onSubmit={handleAdd}>
        <div className="fields two-up">
          <label className="field">
            <span>Date</span>
            <input
              type="date"
              value={form.date}
              onChange={(e) => setForm((prev) => ({ ...prev, date: e.target.value }))}
              disabled={loading}
            />
          </label>
          <label className="field">
            <span>Product</span>
            <select
              value={form.productId}
              onChange={(e) => setForm((prev) => ({ ...prev, productId: e.target.value }))}
              disabled={loading}
            >
              <option value="">Select product</option>
              {products.map((product) => (
                <option key={product.id} value={product.id}>
                  {product.productCode} · {product.material}
                </option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Quantity</span>
            <input
              type="number"
              min="0"
              step="0.1"
              value={form.quantity}
              onChange={(e) => setForm((prev) => ({ ...prev, quantity: e.target.value }))}
              placeholder="0"
              disabled={loading}
            />
          </label>
        </div>
        <div className="actions">
          <button className="primary" type="submit" disabled={loading || !products.length}>
            Add production
          </button>
        </div>
      </form>

      <div className="panel-inner">
        <div className="table-row table-row--header">
          <span>Date</span>
          <span>Product</span>
          <span>Quantity</span>
          <span className="tight">Actions</span>
        </div>
        {sortedProductions.map((item) => {
          const isEditing = editingId === item.id;
          const dateValue = item.dateOfProduction || item.date;
          return (
            <div key={item.id} className={`table-row ${isEditing ? 'is-editing' : ''}`}>
              <span>
                {isEditing ? (
                  <input
                    type="date"
                    value={draft.date}
                    onChange={(e) => setDraft((prev) => ({ ...prev, date: e.target.value }))}
                    disabled={loading}
                  />
                ) : (
                  <strong>{dateValue ? formatDate(dateValue) : '—'}</strong>
                )}
              </span>
              <span>
                {isEditing ? (
                  <select
                    value={draft.productId}
                    onChange={(e) => setDraft((prev) => ({ ...prev, productId: e.target.value }))}
                    disabled={loading}
                  >
                    <option value="">Select product</option>
                    {products.map((product) => (
                      <option key={product.id} value={product.id}>
                        {product.productCode} · {product.material}
                      </option>
                    ))}
                  </select>
                ) : (
                  `${item.product?.productCode ?? '—'} · ${item.product?.material ?? ''}`
                )}
              </span>
              <span className="mono">
                {isEditing ? (
                  <input
                    type="number"
                    min="0"
                    step="0.1"
                    value={draft.quantity}
                    onChange={(e) => setDraft((prev) => ({ ...prev, quantity: e.target.value }))}
                    disabled={loading}
                  />
                ) : (
                  Number(item.quantity).toFixed(2)
                )}
              </span>
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
                    <button className="ghost" onClick={() => startEdit(item)} disabled={loading}>
                      Edit
                    </button>
                    <button
                      className="ghost danger"
                      onClick={() => onDelete(item.id)}
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
        {!sortedProductions.length ? <p className="muted">No production runs recorded.</p> : null}
      </div>
    </div>
  );
}
