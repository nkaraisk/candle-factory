const API_BASE = import.meta.env.VITE_API_BASE_URL || '';

const request = async (url, options = {}) => {
  const response = await fetch(`${API_BASE}${url}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  });

  if (!response.ok) {
    const message = await response.text().catch(() => 'Request failed');
    throw new Error(message || `Request failed (${response.status})`);
  }

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get('content-type') || '';
  if (contentType.includes('application/json')) {
    return response.json();
  }

  return response.text();
};

export const fetchWorkers = () => request('/worker/all', { method: 'GET' });

export const createWorker = (worker) =>
  request('/worker/register', {
    method: 'POST',
    body: JSON.stringify(worker),
  });

export const editWorker = (workerId, worker) =>
  request(`/worker/${workerId}/edit`, {
    method: 'POST',
    body: JSON.stringify(worker),
  });

export const deleteWorker = (workerId) =>
  request(`/worker/${workerId}/delete`, { method: 'DELETE' });

export const fetchLeavesForWorker = (workerId) =>
  request(`/leave/${workerId}/worker`, { method: 'GET' });

export const fetchTodaysLeaves = () => request('/leave/day', { method: 'GET' });

export const requestLeave = (payload) =>
  request('/leave/add', {
    method: 'POST',
    body: JSON.stringify(payload),
  });

export const editLeave = (payload) =>
  request('/leave/edit', {
    method: 'POST',
    body: JSON.stringify(payload),
  });

export const deleteLeave = (leaveId) =>
  request('/leave/delete', {
    method: 'DELETE',
    body: JSON.stringify({ leaveId }),
  });
