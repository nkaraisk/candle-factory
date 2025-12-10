import { useState } from 'react';

const initialState = {
  firstName: '',
  lastName: '',
  phoneNumber: '',
};

export default function WorkerForm({ onSubmit, disabled }) {
  const [form, setForm] = useState(initialState);

  const updateField = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.firstName.trim() || !form.lastName.trim() || !form.phoneNumber.trim()) {
      return;
    }
    await onSubmit(form);
    setForm(initialState);
  };

  return (
    <form className="panel-form" onSubmit={handleSubmit}>
      <div className="fields">
        <label className="field">
          <span>First name</span>
          <input
            name="firstName"
            value={form.firstName}
            onChange={updateField}
            placeholder="Alex"
            disabled={disabled}
          />
        </label>
        <label className="field">
          <span>Last name</span>
          <input
            name="lastName"
            value={form.lastName}
            onChange={updateField}
            placeholder="Markos"
            disabled={disabled}
          />
        </label>
        <label className="field">
          <span>Phone</span>
          <input
            name="phoneNumber"
            value={form.phoneNumber}
            onChange={updateField}
            placeholder="+30 69..."
            disabled={disabled}
          />
        </label>
      </div>
      <div className="actions">
        <button className="primary" type="submit" disabled={disabled}>
          Add worker
        </button>
      </div>
    </form>
  );
}
