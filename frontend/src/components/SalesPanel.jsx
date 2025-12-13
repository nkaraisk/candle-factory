import { useMemo, useState } from 'react';

const empty = { date: '', customerId: '', productId: '', quantity: '', totalCost: '' };

const formatCurrency = (value) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'EUR' }).format(Number(value || 0));

const formatDate = (date) => new Date(`${date}T00:00:00`).toLocaleDateString();

export default function SalesPanel({ sales, customers, products, onAdd, onEdit, onDelete, loading }) {
  const [form, setForm] = useState(empty);
  const [editingId, setEditingId] = useState(null);
  const [draft, setDraft] = useState(empty);

  const sortedSales = useMemo(
    () => [...sales].sort((a, b) => new Date(b.date) - new Date(a.date)),
    [sales],
  );

  const handleAdd = async (e) => {
    e.preventDefault();
    if (!form.date || !form.customerId || !form.productId || !form.quantity) return;
    const payload = {
      date: form.date,
      customerId: Number(form.customerId),
      productId: Number(form.productId),
      quantity: Number(form.quantity),
    };
    if (form.totalCost) payload.totalCost = Number(form.totalCost);
    await onAdd(payload);
    setForm(empty);
  };

  const startEdit = (sale) => {
    setEditingId(sale.id);
    setDraft({
      date: sale.date || '',
      customerId: sale.customer?.id ?? '',
      productId: sale.productType?.id ?? '',
      quantity: sale.quantity,
      totalCost: sale.cost,
    });
  };

  const saveEdit = async () => {
    if (!draft.date || !draft.customerId || !draft.productId || !draft.quantity) return;
    const payload = {
      id: editingId,
      date: draft.date,
      customerId: Number(draft.customerId),
      productId: Number(draft.productId),
      quantity: Number(draft.quantity),
    };
    if (draft.totalCost !== '' && draft.totalCost !== null && draft.totalCost !== undefined) {
      payload.totalCost = Number(draft.totalCost);
    }
    await onEdit(payload);
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
          <p className="eyebrow">Sales</p>
          <h2>Orders</h2>
        </div>
        <span className="pill ghost">{sales.length} records</span>
      </div>

      <form className="panel-form" onSubmit={handleAdd}>
        <div className="fields">
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
            <span>Customer</span>
            <select
              value={form.customerId}
              onChange={(e) => setForm((prev) => ({ ...prev, customerId: e.target.value }))}
              disabled={loading}
            >
              <option value="">Select customer</option>
              {customers.map((customer) => (
                <option key={customer.id} value={customer.id}>
                  {customer.name}
                </option>
              ))}
            </select>
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
          <label className="field">
            <span>Total cost (optional)</span>
            <input
              type="number"
              min="0"
              step="0.01"
              value={form.totalCost}
              onChange={(e) => setForm((prev) => ({ ...prev, totalCost: e.target.value }))}
              placeholder="Auto"
              disabled={loading}
            />
          </label>
        </div>
        <div className="actions">
          <button
            className="primary"
            type="submit"
            disabled={loading || !products.length || !customers.length}
          >
            Add sale
          </button>
        </div>
      </form>

      <div className="panel-inner">
        <div className="table-row table-row--header">
          <span>Date</span>
          <span>Customer</span>
          <span>Product</span>
          <span>Qty</span>
          <span>Cost</span>
          <span className="tight">Actions</span>
        </div>
        {sortedSales.map((sale) => {
          const isEditing = editingId === sale.id;
          return (
            <div key={sale.id} className={`table-row sales-row ${isEditing ? 'is-editing' : ''}`}>
              <span>
                {isEditing ? (
                  <input
                    type="date"
                    value={draft.date}
                    onChange={(e) => setDraft((prev) => ({ ...prev, date: e.target.value }))}
                    disabled={loading}
                  />
                ) : (
                  <strong>{sale.date ? formatDate(sale.date) : '—'}</strong>
                )}
              </span>
              <span>
                {isEditing ? (
                  <select
                    value={draft.customerId}
                    onChange={(e) => setDraft((prev) => ({ ...prev, customerId: e.target.value }))}
                    disabled={loading}
                  >
                    <option value="">Select customer</option>
                    {customers.map((customer) => (
                      <option key={customer.id} value={customer.id}>
                        {customer.name}
                      </option>
                    ))}
                  </select>
                ) : (
                  sale.customer?.name ?? '—'
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
                  `${sale.productType?.productCode ?? '—'} · ${sale.productType?.material ?? ''}`
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
                  sale.quantity
                )}
              </span>
              <span className="mono">
                {isEditing ? (
                  <input
                    type="number"
                    min="0"
                    step="0.01"
                    value={draft.totalCost}
                    onChange={(e) => setDraft((prev) => ({ ...prev, totalCost: e.target.value }))}
                    disabled={loading}
                  />
                ) : (
                  formatCurrency(sale.cost)
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
                    <button className="ghost" onClick={() => startEdit(sale)} disabled={loading}>
                      Edit
                    </button>
                    <button
                      className="ghost danger"
                      onClick={() => onDelete(sale.id)}
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
        {!sortedSales.length ? <p className="muted">No sales recorded.</p> : null}
      </div>
    </div>
  );
}
