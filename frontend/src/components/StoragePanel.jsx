import { useMemo, useState } from 'react';

const formatLabel = (storage) => {
  const code = storage.product?.productCode ?? '—';
  const material = storage.product?.material ?? '';
  return `${code} · ${material}`;
};

export default function StoragePanel({ storageItems, products, onAdjust, onDelete, onAdd, loading }) {
  const [editingId, setEditingId] = useState(null);
  const [draftQty, setDraftQty] = useState('');
  const [form, setForm] = useState({
    productId: '',
    quantity: '',
  });

  const sortedStorage = useMemo(
    () => [...storageItems].sort((a, b) => formatLabel(a).localeCompare(formatLabel(b))),
    [storageItems],
  );

  const handleAdd = async (e) => {
    e.preventDefault();
    if (!form.productId) return;
    await onAdd({ productId: Number(form.productId), quantity: Number(form.quantity || 0) });
    setForm({ productId: '', quantity: '' });
  };

  const startEdit = (item) => {
    setEditingId(item.id);
    setDraftQty(item.quantity);
  };

  const saveEdit = async () => {
    await onAdjust(editingId, Number(draftQty));
    setEditingId(null);
  };

  const cancel = () => {
    setEditingId(null);
    setDraftQty('');
  };

  return (
    <div className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Storage</p>
          <h2>Stock levels</h2>
        </div>
        <span className="pill ghost">{storageItems.length} entries</span>
      </div>

      <form className="panel-form" onSubmit={handleAdd}>
        <div className="fields">
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
            Add storage record
          </button>
        </div>
      </form>

      <div className="panel-inner">
        <div className="table-row table-row--header storage-row">
          <span>Product</span>
          <span>Quantity</span>
          <span className="tight">Actions</span>
        </div>
        {sortedStorage.map((item) => {
          const isEditing = editingId === item.id;
          return (
            <div key={item.id} className={`table-row storage-row ${isEditing ? 'is-editing' : ''}`}>
              <span>{formatLabel(item)}</span>
              <span>
                {isEditing ? (
                  <input
                    type="number"
                    min="0"
                    step="0.1"
                    value={draftQty}
                    onChange={(e) => setDraftQty(e.target.value)}
                    disabled={loading}
                  />
                ) : (
                  <span className="mono">{Number(item.quantity).toFixed(2)}</span>
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
        {!sortedStorage.length ? <p className="muted">No storage entries.</p> : null}
      </div>
    </div>
  );
}
