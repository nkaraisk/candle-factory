import { useMemo, useState } from 'react';

const emptyProduct = {
  productCode: '',
  material: 'Brown',
  byWeight: true,
  price: '',
};

const materialOptions = ['Brown', 'White', 'Pure'];

const formatPrice = (value) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'EUR' }).format(Number(value || 0));

export default function ProductPanel({ products, onAdd, onEdit, onDelete, loading }) {
  const [form, setForm] = useState(emptyProduct);
  const [editingId, setEditingId] = useState(null);
  const [draft, setDraft] = useState(emptyProduct);

  const sortedProducts = useMemo(
    () => [...products].sort((a, b) => a.productCode.localeCompare(b.productCode)),
    [products],
  );

  const handleCreate = async (e) => {
    e.preventDefault();
    if (!form.productCode.trim() || form.price === '') return;
    const payload = { ...form, price: Number(form.price), byWeight: !!form.byWeight };
    await onAdd(payload);
    setForm(emptyProduct);
  };

  const startEdit = (product) => {
    setEditingId(product.id);
    setDraft({
      productCode: product.productCode,
      material: product.material,
      byWeight: product.byWeight,
      price: product.price,
    });
  };

  const saveEdit = async () => {
    if (!draft.productCode.trim() || draft.price === '') return;
    await onEdit({
      productId: editingId,
      productCode: draft.productCode,
      material: draft.material,
      byWeight: !!draft.byWeight,
      price: Number(draft.price),
    });
    setEditingId(null);
  };

  const cancelEdit = () => {
    setEditingId(null);
    setDraft(emptyProduct);
  };

  return (
    <div className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Products</p>
          <h2>Catalog</h2>
        </div>
      </div>

      <form className="panel-form" onSubmit={handleCreate}>
        <div className="fields">
          <label className="field">
            <span>Code</span>
            <input
              name="productCode"
              value={form.productCode}
              onChange={(e) => setForm((prev) => ({ ...prev, productCode: e.target.value }))}
              placeholder="e.g. L40"
              disabled={loading}
            />
          </label>
          <label className="field">
            <span>Material</span>
            <select
              name="material"
              value={form.material}
              onChange={(e) => setForm((prev) => ({ ...prev, material: e.target.value }))}
              disabled={loading}
            >
              {materialOptions.map((option) => (
                <option key={option} value={option}>
                  {option}
                </option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Pricing</span>
            <select
              name="byWeight"
              value={form.byWeight ? 'true' : 'false'}
              onChange={(e) => setForm((prev) => ({ ...prev, byWeight: e.target.value === 'true' }))}
              disabled={loading}
            >
              <option value="true">By weight</option>
              <option value="false">By unit</option>
            </select>
          </label>
          <label className="field">
            <span>Price</span>
            <input
              type="number"
              step="0.01"
              min="0"
              name="price"
              value={form.price}
              onChange={(e) => setForm((prev) => ({ ...prev, price: e.target.value }))}
              placeholder="12.50"
              disabled={loading}
            />
          </label>
        </div>
        <div className="actions">
          <button className="primary" type="submit" disabled={loading}>
            Add product
          </button>
        </div>
      </form>

      <div className="panel-inner">
        <div className="table-row table-row--header">
          <span>Product</span>
          <span>Material</span>
          <span>Price</span>
          <span className="tight">Actions</span>
        </div>
        {sortedProducts.map((product) => {
          const isEditing = editingId === product.id;
          return (
            <div key={product.id} className={`table-row ${isEditing ? 'is-editing' : ''}`}>
              <span>
                {isEditing ? (
                  <input
                    value={draft.productCode}
                    onChange={(e) => setDraft((prev) => ({ ...prev, productCode: e.target.value }))}
                    disabled={loading}
                  />
                ) : (
                  <strong>{product.productCode}</strong>
                )}
              </span>
              <span>
                {isEditing ? (
                  <select
                    value={draft.material}
                    onChange={(e) => setDraft((prev) => ({ ...prev, material: e.target.value }))}
                    disabled={loading}
                  >
                    {materialOptions.map((option) => (
                      <option key={option} value={option}>
                        {option}
                      </option>
                    ))}
                  </select>
                ) : (
                  product.material
                )}
              </span>
              <span>
                {isEditing ? (
                  <div className="edit-pricing">
                    <select
                      value={draft.byWeight ? 'true' : 'false'}
                      onChange={(e) =>
                        setDraft((prev) => ({ ...prev, byWeight: e.target.value === 'true' }))
                      }
                      disabled={loading}
                    >
                      <option value="true">By weight</option>
                      <option value="false">By unit</option>
                    </select>
                    <input
                      type="number"
                      step="0.01"
                      min="0"
                      value={draft.price}
                      onChange={(e) => setDraft((prev) => ({ ...prev, price: e.target.value }))}
                      disabled={loading}
                    />
                  </div>
                ) : (
                  <div className="mono">
                    {formatPrice(product.price)} · {product.byWeight ? 'per kg' : 'per unit'}
                  </div>
                )}
              </span>
              <span className="actions tight">
                {isEditing ? (
                  <>
                    <button className="primary" onClick={saveEdit} disabled={loading}>
                      Save
                    </button>
                    <button className="ghost" onClick={cancelEdit} disabled={loading}>
                      Cancel
                    </button>
                  </>
                ) : (
                  <>
                    <button className="ghost" onClick={() => startEdit(product)} disabled={loading}>
                      Edit
                    </button>
                    <button
                      className="ghost danger"
                      onClick={() => onDelete(product.id)}
                      disabled={loading}
                    >
                      Remove
                    </button>
                  </>
                )}
              </span>
            </div>
          );
        })}
        {!sortedProducts.length ? <p className="muted">No products yet.</p> : null}
      </div>
    </div>
  );
}
