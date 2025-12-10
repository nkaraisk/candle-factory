import { useState } from 'react';

const initial = { startDate: '', endDate: '' };

export default function LeaveForm({ onSubmit, disabled, workerLabel }) {
  const [form, setForm] = useState(initial);

  const update = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.startDate || !form.endDate) {
      return;
    }
    await onSubmit(form);
    setForm(initial);
  };

  return (
    <form className="panel-form" onSubmit={handleSubmit}>
      <div className="fields two-up">
        <label className="field">
          <span>Start date</span>
          <input type="date" name="startDate" value={form.startDate} onChange={update} disabled={disabled} />
        </label>
        <label className="field">
          <span>End date</span>
          <input type="date" name="endDate" value={form.endDate} onChange={update} disabled={disabled} />
        </label>
      </div>
      <div className="actions">
        <button className="primary" type="submit" disabled={disabled}>
          Request leave {workerLabel ? `for ${workerLabel}` : ''}
        </button>
      </div>
    </form>
  );
}
