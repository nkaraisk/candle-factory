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

// Products
export const fetchProducts = () => request('/product/getAll', { method: 'GET' });

export const addProduct = (product) =>
  request('/product/add', {
    method: 'POST',
    body: JSON.stringify(product),
  });

export const updateProduct = (product) =>
  request('/product/edit', {
    method: 'PUT',
    body: JSON.stringify(product),
  });

export const deleteProduct = (productId, { hard = false } = {}) =>
  request(`/product/${productId}/${hard ? 'admin/delete' : 'delete'}`, { method: 'DELETE' });

// Storage
export const fetchStorage = () => request('/storage/getAll', { method: 'GET' });

export const addStorage = (storage) =>
  request('/storage/add', {
    method: 'POST',
    body: JSON.stringify(storage),
  });

export const updateStorageQuantity = (storageId, quantity) =>
  request(`/storage/${storageId}/edit?quantity=${quantity}`, { method: 'PUT' });

export const deleteStorage = (storageId) => request(`/storage/${storageId}/delete`, { method: 'DELETE' });

// Production
export const fetchProductions = () => request('/production/getAll', { method: 'GET' });

export const addProduction = (production) =>
  request('/production/add', {
    method: 'POST',
    body: JSON.stringify(production),
  });

export const updateProduction = (production) =>
  request('/production/edit', {
    method: 'PUT',
    body: JSON.stringify(production),
  });

export const deleteProduction = (productionId) =>
  request(`/production/${productionId}/delete`, { method: 'DELETE' });

// Sales
export const fetchSales = () => request('/sale/getAll', { method: 'GET' });

export const addSale = (sale) =>
  request('/sale/add', {
    method: 'POST',
    body: JSON.stringify(sale),
  });

export const updateSale = (sale) =>
  request('/sale/edit', {
    method: 'PUT',
    body: JSON.stringify(sale),
  });

export const deleteSale = (saleId) => request(`/sale/${saleId}/delete`, { method: 'DELETE' });

// Customers
export const fetchCustomers = () => request('/customer/all', { method: 'GET' });

export const addCustomer = (customer) =>
  request('/customer/add', {
    method: 'POST',
    body: JSON.stringify(customer),
  });

export const updateCustomer = (customer) =>
  request('/customer/edit', {
    method: 'POST',
    body: JSON.stringify(customer),
  });

export const deleteCustomer = (customerId) =>
  request(`/customer/${customerId}/delete`, { method: 'DELETE' });
