import { useEffect, useMemo, useState } from 'react';
import './App.css';
import {
  createWorker,
  createCustomer,
  createProduct,
  createProduction,
  createReturn,
  createSale,
  deleteLeave,
  deleteWorker,
  deleteCustomer,
  deleteProduct,
  deleteProduction,
  deleteReturn,
  deleteSale,
  editLeave,
  editWorker,
  fetchLeavesForWorker,
  fetchTodaysLeaves,
  fetchWorkers,
  fetchCustomers,
  fetchProducts,
  fetchProductions,
  fetchReturns,
  fetchSales,
  requestLeave,
  searchWorkerByFirstName,
  searchWorkerByFullName,
  searchWorkerByLastName,
  searchWorkerByPhone,
} from './api/client';
import LeaveForm from './components/LeaveForm';
import LeaveTable from './components/LeaveTable';
import WorkerForm from './components/WorkerForm';
import WorkerList from './components/WorkerList';

function App() {
  const [workers, setWorkers] = useState([]);
  const [selectedWorkerId, setSelectedWorkerId] = useState(null);
  const [leaves, setLeaves] = useState([]);
  const [todayLeaves, setTodayLeaves] = useState([]);
  const [status, setStatus] = useState(null);
  const [loadingWorkers, setLoadingWorkers] = useState(false);
  const [loadingLeaves, setLoadingLeaves] = useState(false);
  const [searchForm, setSearchForm] = useState({ firstName: '', lastName: '', phoneNumber: '' });
  const [searchResults, setSearchResults] = useState([]);
  const [searchContext, setSearchContext] = useState('');
  const [searching, setSearching] = useState(false);
  const [activeView, setActiveView] = useState('team');
  const [products, setProducts] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [productions, setProductions] = useState([]);
  const [sales, setSales] = useState([]);
  const [returns, setReturns] = useState([]);
  const [loadingOps, setLoadingOps] = useState(false);
  const [inventoryForm, setInventoryForm] = useState({
    productCode: '',
    material: 'Brown',
    byWeight: true,
    price: 0,
  });
  const [customerForm, setCustomerForm] = useState({ customerName: '', customerPhone: '', debt: 0 });
  const [productionForm, setProductionForm] = useState({
    productId: '',
    quantity: 0,
    date: '',
  });
  const [saleForm, setSaleForm] = useState({ customerId: '', productId: '', quantity: 0, date: '' });
  const [returnForm, setReturnForm] = useState({
    customerId: '',
    material: 'Brown',
    weight: 0,
    returnDate: '',
    note: '',
  });

  const selectedWorker = useMemo(
    () => workers.find((worker) => worker.id === selectedWorkerId) || null,
    [workers, selectedWorkerId],
  );

  useEffect(() => {
    refreshWorkers(true);
    refreshTodayLeaves();
    refreshOps();
  }, []);

  useEffect(() => {
    if (selectedWorkerId) {
      loadLeaves(selectedWorkerId);
    } else {
      setLeaves([]);
    }
  }, [selectedWorkerId]);

  const pushStatus = (type, message) => setStatus({ type, message, ts: Date.now() });

  const updateSearchField = (e) => {
    const { name, value } = e.target;
    setSearchForm((prev) => ({ ...prev, [name]: value }));
  };

  const normalizeWorkers = (payload) => {
    if (!payload) return [];
    return Array.isArray(payload) ? payload : [payload];
  };

  const refreshWorkers = async (keepSelection = false) => {
    setLoadingWorkers(true);
    try {
      const data = await fetchWorkers();
      setWorkers(data);

      const hasSelection = data.some((worker) => worker.id === selectedWorkerId);
      if (!keepSelection || !hasSelection) {
        setSelectedWorkerId(data[0]?.id ?? null);
      }
    } catch (err) {
      setWorkers([]);
      setSelectedWorkerId(null);
      pushStatus('error', err.message);
    } finally {
      setLoadingWorkers(false);
    }
  };

  const loadLeaves = async (workerId) => {
    setLoadingLeaves(true);
    try {
      const data = await fetchLeavesForWorker(workerId);
      setLeaves(data);
    } catch (err) {
      setLeaves([]);
      pushStatus('info', err.message);
    } finally {
      setLoadingLeaves(false);
    }
  };

  const refreshTodayLeaves = async () => {
    try {
      const data = await fetchTodaysLeaves();
      setTodayLeaves(data);
    } catch {
      setTodayLeaves([]);
    }
  };

  const refreshOps = async () => {
    setLoadingOps(true);
    try {
      const [productData, customerData, productionData, salesData, returnsData] = await Promise.all([
        fetchProducts(),
        fetchCustomers(),
        fetchProductions(),
        fetchSales(),
        fetchReturns(),
      ]);
      setProducts(productData);
      setCustomers(customerData);
      setProductions(productionData);
      setSales(salesData);
      setReturns(returnsData);
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleSearch = async (mode) => {
    setSearching(true);
    try {
      let result;
      let label = '';

      if (mode === 'full') {
        if (!searchForm.firstName.trim() || !searchForm.lastName.trim()) {
          pushStatus('info', 'Add both first and last name to search by full name.');
          return;
        }
        result = await searchWorkerByFullName({
          firstName: searchForm.firstName.trim(),
          lastName: searchForm.lastName.trim(),
        });
        label = 'Full name';
      }
      if (mode === 'first') {
        if (!searchForm.firstName.trim()) {
          pushStatus('info', 'Add a first name to run this search.');
          return;
        }
        result = await searchWorkerByFirstName({ firstName: searchForm.firstName.trim() });
        label = 'First name';
      }
      if (mode === 'last') {
        if (!searchForm.lastName.trim()) {
          pushStatus('info', 'Add a surname to run this search.');
          return;
        }
        result = await searchWorkerByLastName({ lastName: searchForm.lastName.trim() });
        label = 'Surname';
      }
      if (mode === 'phone') {
        if (!searchForm.phoneNumber.trim()) {
          pushStatus('info', 'Add a phone number to run this search.');
          return;
        }
        result = await searchWorkerByPhone({ phoneNumber: searchForm.phoneNumber.trim() });
        label = 'Phone number';
      }

      const normalized = normalizeWorkers(result);
      setSearchResults(normalized);
      setSearchContext(label);

      if (!normalized.length) {
        pushStatus('info', 'No matches found for that search.');
      }
    } catch (err) {
      setSearchResults([]);
      pushStatus('error', err.message);
    } finally {
      setSearching(false);
    }
  };

  const resetSearch = () => {
    setSearchResults([]);
    setSearchContext('');
    setSearchForm({ firstName: '', lastName: '', phoneNumber: '' });
  };

  const focusWorker = async (workerId) => {
    if (!workerId) return;
    if (!workers.some((worker) => worker.id === workerId)) {
      await refreshWorkers(true);
    }
    setSelectedWorkerId(workerId);
  };

  const updateInventoryField = (e) => {
    const { name, value, type, checked } = e.target;
    setInventoryForm((prev) => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };

  const updateCustomerField = (e) => {
    const { name, value } = e.target;
    setCustomerForm((prev) => ({ ...prev, [name]: value }));
  };

  const updateProductionField = (e) => {
    const { name, value } = e.target;
    setProductionForm((prev) => ({ ...prev, [name]: value }));
  };

  const updateSaleField = (e) => {
    const { name, value } = e.target;
    setSaleForm((prev) => ({ ...prev, [name]: value }));
  };

  const updateReturnField = (e) => {
    const { name, value } = e.target;
    setReturnForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleCreateWorker = async (payload) => {
    setLoadingWorkers(true);
    try {
      await createWorker(payload);
      pushStatus('success', 'Worker registered.');
      await refreshWorkers();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingWorkers(false);
    }
  };

  const handleEditWorker = async (id, payload) => {
    setLoadingWorkers(true);
    try {
      await editWorker(id, payload);
      pushStatus('success', 'Worker updated.');
      await refreshWorkers(true);
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingWorkers(false);
    }
  };

  const handleDeleteWorker = async (id) => {
    if (!window.confirm('Delete this worker and all related leave entries?')) return;
    setLoadingWorkers(true);
    try {
      await deleteWorker(id);
      pushStatus('success', 'Worker deleted.');
      await refreshWorkers();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingWorkers(false);
    }
  };

  const handleLeaveRequest = async (form) => {
    if (!selectedWorker) {
      pushStatus('info', 'Select a worker first.');
      return;
    }

    setLoadingLeaves(true);
    try {
      await requestLeave({ ...form, workerId: selectedWorker.id });
      pushStatus('success', 'Leave saved.');
      await loadLeaves(selectedWorker.id);
      await refreshWorkers(true);
      await refreshTodayLeaves();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingLeaves(false);
    }
  };

  const handleLeaveEdit = async (payload) => {
    setLoadingLeaves(true);
    try {
      await editLeave(payload);
      pushStatus('success', 'Leave updated.');
      if (selectedWorkerId) {
        await loadLeaves(selectedWorkerId);
      }
      await refreshWorkers(true);
      await refreshTodayLeaves();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingLeaves(false);
    }
  };

  const handleLeaveDelete = async (leaveId) => {
    if (!window.confirm('Delete this leave?')) return;
    setLoadingLeaves(true);
    try {
      await deleteLeave(leaveId);
      pushStatus('success', 'Leave deleted.');
      if (selectedWorkerId) {
        await loadLeaves(selectedWorkerId);
      }
      await refreshWorkers(true);
      await refreshTodayLeaves();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingLeaves(false);
    }
  };

  const handleProductCreate = async (e) => {
    e.preventDefault();
    if (!inventoryForm.productCode.trim()) return;
    setLoadingOps(true);
    try {
      await createProduct({
        productCode: inventoryForm.productCode.trim(),
        material: inventoryForm.material,
        byWeight: Boolean(inventoryForm.byWeight),
        price: Number(inventoryForm.price),
      });
      pushStatus('success', 'Product saved.');
      setInventoryForm({ productCode: '', material: 'Brown', byWeight: true, price: 0 });
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleProductDelete = async (id) => {
    if (!window.confirm('Delete this product?')) return;
    setLoadingOps(true);
    try {
      await deleteProduct(id);
      pushStatus('success', 'Product removed.');
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleCustomerCreate = async (e) => {
    e.preventDefault();
    if (!customerForm.customerName.trim() || !customerForm.customerPhone.trim()) return;
    setLoadingOps(true);
    try {
      await createCustomer({
        customerName: customerForm.customerName.trim(),
        customerPhone: customerForm.customerPhone.trim(),
        debt: Number(customerForm.debt),
      });
      pushStatus('success', 'Customer saved.');
      setCustomerForm({ customerName: '', customerPhone: '', debt: 0 });
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleCustomerDelete = async (id) => {
    if (!window.confirm('Delete this customer?')) return;
    setLoadingOps(true);
    try {
      await deleteCustomer(id);
      pushStatus('success', 'Customer removed.');
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleProductionCreate = async (e) => {
    e.preventDefault();
    if (!productionForm.productId || !productionForm.quantity) return;
    setLoadingOps(true);
    try {
      await createProduction({
        productId: Number(productionForm.productId),
        quantity: Number(productionForm.quantity),
        date: productionForm.date || null,
      });
      pushStatus('success', 'Production logged.');
      setProductionForm({ productId: '', quantity: 0, date: '' });
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleProductionDelete = async (id) => {
    if (!window.confirm('Delete this production entry?')) return;
    setLoadingOps(true);
    try {
      await deleteProduction(id);
      pushStatus('success', 'Production removed.');
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleSaleCreate = async (e) => {
    e.preventDefault();
    if (!saleForm.customerId || !saleForm.productId || !saleForm.quantity) return;
    setLoadingOps(true);
    try {
      await createSale({
        customerId: Number(saleForm.customerId),
        productId: Number(saleForm.productId),
        quantity: Number(saleForm.quantity),
        date: saleForm.date || null,
      });
      pushStatus('success', 'Sale recorded.');
      setSaleForm({ customerId: '', productId: '', quantity: 0, date: '' });
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleSaleDelete = async (id) => {
    if (!window.confirm('Delete this sale?')) return;
    setLoadingOps(true);
    try {
      await deleteSale(id);
      pushStatus('success', 'Sale removed.');
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleReturnCreate = async (e) => {
    e.preventDefault();
    if (!returnForm.customerId || !returnForm.weight) return;
    setLoadingOps(true);
    try {
      await createReturn({
        customerId: Number(returnForm.customerId),
        weight: Number(returnForm.weight),
        material: returnForm.material,
        returnDate: returnForm.returnDate || null,
        note: returnForm.note,
      });
      pushStatus('success', 'Return logged.');
      setReturnForm({ customerId: '', material: 'Brown', weight: 0, returnDate: '', note: '' });
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const handleReturnDelete = async (id) => {
    if (!window.confirm('Delete this return entry?')) return;
    setLoadingOps(true);
    try {
      await deleteReturn(id);
      pushStatus('success', 'Return removed.');
      await refreshOps();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingOps(false);
    }
  };

  const todayOffCount = todayLeaves.length;
  const availableWorkers = Math.max(workers.length - todayOffCount, 0);
  const mostLeaveHeavy = [...workers]
    .sort((a, b) => (b.daysOfLeave ?? 0) - (a.daysOfLeave ?? 0))
    .slice(0, 3);
  const viewTabs = [
    { id: 'team', label: 'Team & Leave', badge: workers.length },
    { id: 'lookup', label: 'Lookup', badge: searchResults.length || '·' },
    { id: 'inventory', label: 'Inventory', badge: products.length },
    { id: 'crm', label: 'CRM & Sales', badge: customers.length },
  ];

  return (
    <div className="app-shell">
      <div className="hero">
        <div>
          <p className="eyebrow">Candle Factory · Ops cockpit</p>
          <h1>Workers &amp; leave at a glance</h1>
          <p className="muted">
            Register the team, plan time off, and keep visibility on today&apos;s staffing.
          </p>
          <div className="hero-chips">
            <span className="pill">{workers.length} workers</span>
            <span className="pill accent">{todayOffCount} off today</span>
            <span className="pill ghost">{availableWorkers} available</span>
          </div>
        </div>
        <div className="hero-actions">
          <button className="ghost" onClick={() => refreshWorkers(true)} disabled={loadingWorkers}>
            Refresh workers
          </button>
          <button className="primary" onClick={refreshTodayLeaves} disabled={loadingLeaves}>
            Sync leave
          </button>
        </div>
      </div>

      {status ? (
        <div key={status.ts} className={`status status--${status.type}`}>
          {status.message}
        </div>
      ) : null}

      <div className="view-tabs">
        {viewTabs.map((tab) => (
          <button
            key={tab.id}
            className={`view-tab ${activeView === tab.id ? 'is-active' : ''}`}
            onClick={() => setActiveView(tab.id)}
            type="button"
          >
            <span>{tab.label}</span>
            <span className="pill ghost tiny">{tab.badge}</span>
          </button>
        ))}
      </div>

      {activeView === 'team' ? (
      <div className="insights">
        <div className="insight-card">
          <p className="eyebrow">Availability</p>
          <div className="stat-line">
            <span className="stat-number">{availableWorkers}</span>
            <span className="pill ghost tiny">ready now</span>
          </div>
          <p className="muted small">
            {workers.length} total · {todayOffCount} out today
          </p>
        </div>
        <div className="insight-card">
          <p className="eyebrow">Focus</p>
          {selectedWorker ? (
            <div className="focus-card">
              <p className="strong">
                {selectedWorker.firstName} {selectedWorker.lastName}
              </p>
              <p className="muted small">Phone {selectedWorker.phoneNumber}</p>
              <div className="stat-line">
                <span className="stat-number tiny">{selectedWorker.daysOfLeave ?? 0}</span>
                <span className="pill ghost tiny">days of leave used</span>
              </div>
            </div>
          ) : (
            <p className="muted small">Pick a worker to see a quick snapshot.</p>
          )}
        </div>
        <div className="insight-card">
          <p className="eyebrow">Heavy leave takers</p>
          <ul className="mini-list">
            {mostLeaveHeavy.length ? (
              mostLeaveHeavy.map((worker) => (
                <li key={worker.id} className="list-item compact">
                  <div>
                    <p className="strong">
                      {worker.firstName} {worker.lastName}
                    </p>
                    <p className="muted small">{worker.phoneNumber}</p>
                  </div>
                  <span className="pill accent">{worker.daysOfLeave ?? 0} days</span>
                </li>
              ))
            ) : (
              <li className="muted small">No leave has been logged yet.</li>
            )}
          </ul>
        </div>
      </div>
      ) : null}

      {activeView === 'lookup' ? (
      <section className="panel spotlight">
        <div className="panel-header">
          <div>
            <p className="eyebrow">Lookup</p>
            <h2>Find workers via every endpoint</h2>
            <p className="muted small">
              Full-name, name, surname or phone-number search wired straight to the backend.
            </p>
          </div>
          <div className="spotlight-actions">
            <button
              className="ghost"
              onClick={resetSearch}
              disabled={
                !searchResults.length &&
                !searchContext &&
                !searchForm.firstName &&
                !searchForm.lastName &&
                !searchForm.phoneNumber
              }
            >
              Reset
            </button>
            <span className={`pill ${searching ? 'accent' : 'ghost'}`}>
              {searching ? 'Searching...' : searchContext ? `${searchResults.length} result(s)` : 'Idle'}
            </span>
          </div>
        </div>

        <div className="search-grid">
          <div className="search-card wide">
            <div className="card-header">
              <p className="strong">Full name</p>
              <p className="muted small">/worker/fullName</p>
            </div>
            <div className="fields two-up">
              <label className="field">
                <span>First name</span>
                <input
                  name="firstName"
                  value={searchForm.firstName}
                  onChange={updateSearchField}
                  placeholder="Alex"
                />
              </label>
              <label className="field">
                <span>Last name</span>
                <input
                  name="lastName"
                  value={searchForm.lastName}
                  onChange={updateSearchField}
                  placeholder="Markos"
                />
              </label>
            </div>
            <div className="actions">
              <button className="primary" type="button" onClick={() => handleSearch('full')} disabled={searching}>
                Search full name
              </button>
            </div>
          </div>

          <div className="search-card">
            <div className="card-header">
              <p className="strong">First name</p>
              <p className="muted small">/worker/firsName</p>
            </div>
            <label className="field">
              <span>First name</span>
              <input
                name="firstName"
                value={searchForm.firstName}
                onChange={updateSearchField}
                placeholder="Maria"
              />
            </label>
            <div className="actions">
              <button className="ghost" type="button" onClick={() => handleSearch('first')} disabled={searching}>
                Search
              </button>
            </div>
          </div>

          <div className="search-card">
            <div className="card-header">
              <p className="strong">Surname</p>
              <p className="muted small">/worker/surname</p>
            </div>
            <label className="field">
              <span>Last name</span>
              <input
                name="lastName"
                value={searchForm.lastName}
                onChange={updateSearchField}
                placeholder="Papadopoulos"
              />
            </label>
            <div className="actions">
              <button className="ghost" type="button" onClick={() => handleSearch('last')} disabled={searching}>
                Search
              </button>
            </div>
          </div>

          <div className="search-card">
            <div className="card-header">
              <p className="strong">Phone number</p>
              <p className="muted small">/worker/phoneNumber</p>
            </div>
            <label className="field">
              <span>Phone</span>
              <input
                name="phoneNumber"
                value={searchForm.phoneNumber}
                onChange={updateSearchField}
                placeholder="+30 69..."
              />
            </label>
            <div className="actions">
              <button className="ghost" type="button" onClick={() => handleSearch('phone')} disabled={searching}>
                Search
              </button>
            </div>
          </div>
        </div>

        <div className="search-results">
          <div className="panel-header subtle">
            <div>
              <p className="eyebrow">Results</p>
              <h3>{searchContext || 'Awaiting query'}</h3>
            </div>
            {searchResults.length ? (
              <button
                className="ghost"
                type="button"
                onClick={() => focusWorker(searchResults[0].id)}
                disabled={!searchResults[0].id}
              >
                Focus first match
              </button>
            ) : null}
          </div>
          {searchResults.length ? (
            <div className="result-grid">
              {searchResults.map((worker) => (
                <div key={`${worker.id ?? worker.phoneNumber}`} className="result-card">
                  <div className="result-head">
                    <p className="strong">
                      {worker.firstName} {worker.lastName}
                    </p>
                    <span className="pill ghost mono">ID {worker.id ?? '—'}</span>
                  </div>
                  <p className="muted small">Phone {worker.phoneNumber ?? '—'}</p>
                  <div className="result-actions">
                    <span className="pill accent">{worker.daysOfLeave ?? 0} days leave</span>
                    <button className="primary" type="button" onClick={() => focusWorker(worker.id)} disabled={!worker.id}>
                      Focus worker
                    </button>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="muted small">Run any of the lookup endpoints to see matches.</p>
          )}
        </div>
      </section>
      ) : null}

      {activeView === 'team' ? (
      <div className="grid">
        <section className="panel">
          <div className="panel-header">
            <div>
              <p className="eyebrow">People</p>
              <h2>Manage workers</h2>
            </div>
          </div>
          <WorkerForm onSubmit={handleCreateWorker} disabled={loadingWorkers} />
          <WorkerList
            workers={workers}
            selectedWorkerId={selectedWorkerId}
            onSelect={setSelectedWorkerId}
            onDelete={handleDeleteWorker}
            onEdit={handleEditWorker}
            loading={loadingWorkers}
          />
        </section>

        <section className="panel">
          <div className="panel-header">
            <div>
              <p className="eyebrow">Leave planner</p>
              <h2>Time off</h2>
              {selectedWorker ? (
                <p className="muted small">
                  Working on {selectedWorker.firstName} {selectedWorker.lastName}
                </p>
              ) : (
                <p className="muted small">Select a worker to view or request leave.</p>
              )}
            </div>
          </div>
          <LeaveForm
            onSubmit={handleLeaveRequest}
            disabled={!selectedWorker || loadingLeaves}
            workerLabel={selectedWorker ? `${selectedWorker.firstName} ${selectedWorker.lastName}` : ''}
          />
          <div className="panel-inner">
            {selectedWorker ? (
              <LeaveTable
                leaves={leaves}
                onDelete={handleLeaveDelete}
                onEdit={handleLeaveEdit}
                loading={loadingLeaves}
              />
            ) : (
              <p className="muted">Pick a worker to load their leave history.</p>
            )}
          </div>
        </section>

        <section className="panel slim">
          <div className="panel-header">
            <div>
              <p className="eyebrow">Today</p>
              <h2>Who is out</h2>
            </div>
            <span className="pill accent">{todayOffCount}</span>
          </div>
          <div className="panel-inner">
            {todayLeaves.length ? (
              <ul className="list">
                {todayLeaves.map((leave) => (
                  <li key={leave.id} className="list-item">
                    <div>
                      <p className="strong">
                        {leave.worker ? `${leave.worker.firstName} ${leave.worker.lastName}` : '—'}
                      </p>
                      <p className="muted small">
                        {leave.startDate} → {leave.endDate}
                      </p>
                    </div>
                    <span className="pill ghost">{leave.worker?.daysOfLeave ?? '—'} days used</span>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="muted">No absences recorded for today.</p>
            )}
          </div>
        </section>
      </div>
      ) : null}

      {(activeView === 'inventory' || activeView === 'crm') ? (
      <div className="grid ops-grid">
        {activeView === 'inventory' ? (
        <section className="panel">
          <div className="panel-header">
            <div>
              <p className="eyebrow">Inventory</p>
              <h2>Products &amp; production</h2>
              <p className="muted small">Material, by-weight flag, and price all map to the new backend endpoints.</p>
            </div>
            <span className="pill ghost">{products.length} products</span>
          </div>

          <form className="panel-form" onSubmit={handleProductCreate}>
            <div className="fields two-up">
              <label className="field">
                <span>Code</span>
                <input
                  name="productCode"
                  value={inventoryForm.productCode}
                  onChange={updateInventoryField}
                  placeholder="WX-24"
                />
              </label>
              <label className="field">
                <span>Material</span>
                <select name="material" value={inventoryForm.material} onChange={updateInventoryField}>
                  <option>Brown</option>
                  <option>White</option>
                  <option>Pure</option>
                </select>
              </label>
              <label className="field">
                <span>Price</span>
                <input
                  type="number"
                  step="0.01"
                  name="price"
                  value={inventoryForm.price}
                  onChange={updateInventoryField}
                />
              </label>
              <label className="field checkbox-field">
                <span>Sold by weight</span>
                <input
                  type="checkbox"
                  name="byWeight"
                  checked={inventoryForm.byWeight}
                  onChange={updateInventoryField}
                />
              </label>
            </div>
            <div className="actions">
              <button className="primary" type="submit" disabled={loadingOps}>
                Save product
              </button>
              <button className="ghost" type="button" onClick={refreshOps} disabled={loadingOps}>
                Refresh
              </button>
            </div>
          </form>

          <div className="table">
            <div className="table-row table-row--header">
              <span>Code</span>
              <span>Material</span>
              <span>Price</span>
              <span className="tight">Actions</span>
            </div>
            {products.map((product) => (
              <div key={product.id} className="table-row">
                <span className="strong">{product.productCode}</span>
                <span>{product.material}</span>
                <span className="mono">
                  €{product.price} {product.byWeight ? '/kg' : ''}
                </span>
                <span className="actions tight">
                  <button className="ghost danger" type="button" onClick={() => handleProductDelete(product.id)}>
                    Delete
                  </button>
                </span>
              </div>
            ))}
            {!products.length ? <p className="muted small">No products yet.</p> : null}
          </div>

          <div className="panel-inner">
            <div className="panel-header">
              <div>
                <p className="eyebrow">Factory</p>
                <h3>Production log</h3>
              </div>
              <span className="pill ghost">{productions.length}</span>
            </div>
            <form className="panel-form" onSubmit={handleProductionCreate}>
              <div className="fields two-up">
                <label className="field">
                  <span>Product</span>
                  <select name="productId" value={productionForm.productId} onChange={updateProductionField}>
                    <option value="">Select product</option>
                    {products.map((p) => (
                      <option key={p.id} value={p.id}>
                        {p.productCode}
                      </option>
                    ))}
                  </select>
                </label>
                <label className="field">
                  <span>Quantity</span>
                  <input
                    type="number"
                    step="0.01"
                    name="quantity"
                    value={productionForm.quantity}
                    onChange={updateProductionField}
                  />
                </label>
                <label className="field">
                  <span>Date</span>
                  <input
                    type="date"
                    name="date"
                    value={productionForm.date}
                    onChange={updateProductionField}
                  />
                </label>
              </div>
              <div className="actions">
                <button className="primary" type="submit" disabled={loadingOps}>
                  Log production
                </button>
              </div>
            </form>
            <div className="list">
              {productions.map((prod) => (
                <div key={prod.id} className="list-item">
                  <div>
                    <p className="strong">#{prod.id}</p>
                    <p className="muted small">
                      {prod.dateOfProduction} · {prod.product?.productCode ?? '—'}
                    </p>
                  </div>
                  <div className="actions">
                    <span className="pill ghost mono">{prod.quantity} qty</span>
                    <button className="ghost danger" type="button" onClick={() => handleProductionDelete(prod.id)}>
                      Delete
                    </button>
                  </div>
                </div>
              ))}
              {!productions.length ? <p className="muted small">No production entries yet.</p> : null}
            </div>
          </div>
        </section>
        ) : null}

        {activeView === 'crm' ? (
        <section className="panel">
          <div className="panel-header">
            <div>
              <p className="eyebrow">Customers &amp; sales</p>
              <h2>CRM + Returns</h2>
            </div>
            <span className="pill ghost">{customers.length} customers</span>
          </div>

          <form className="panel-form" onSubmit={handleCustomerCreate}>
            <div className="fields two-up">
              <label className="field">
                <span>Name</span>
                <input
                  name="customerName"
                  value={customerForm.customerName}
                  onChange={updateCustomerField}
                  placeholder="Customer"
                />
              </label>
              <label className="field">
                <span>Phone</span>
                <input
                  name="customerPhone"
                  value={customerForm.customerPhone}
                  onChange={updateCustomerField}
                  placeholder="+30"
                />
              </label>
              <label className="field">
                <span>Debt</span>
                <input
                  type="number"
                  step="0.01"
                  name="debt"
                  value={customerForm.debt}
                  onChange={updateCustomerField}
                />
              </label>
            </div>
            <div className="actions">
              <button className="primary" type="submit" disabled={loadingOps}>
                Save customer
              </button>
            </div>
          </form>

          <div className="table">
            <div className="table-row table-row--header">
              <span>Name</span>
              <span>Phone</span>
              <span>Balance</span>
              <span className="tight">Actions</span>
            </div>
            {customers.map((customer) => (
              <div key={customer.id} className="table-row">
                <span className="strong">{customer.name}</span>
                <span className="mono">{customer.phoneNumber}</span>
                <span className="mono">
                  €{Number(customer.debt ?? 0) - Number(customer.credit ?? 0)}
                </span>
                <span className="actions tight">
                  <button className="ghost danger" type="button" onClick={() => handleCustomerDelete(customer.id)}>
                    Delete
                  </button>
                </span>
              </div>
            ))}
            {!customers.length ? <p className="muted small">No customers yet.</p> : null}
          </div>

          <div className="panel-inner stacked">
            <div className="panel-header">
              <div>
                <p className="eyebrow">Sales</p>
                <h3>Record sale</h3>
              </div>
              <span className="pill ghost">{sales.length}</span>
            </div>
            <form className="panel-form" onSubmit={handleSaleCreate}>
              <div className="fields two-up">
                <label className="field">
                  <span>Customer</span>
                  <select name="customerId" value={saleForm.customerId} onChange={updateSaleField}>
                    <option value="">Select customer</option>
                    {customers.map((c) => (
                      <option key={c.id} value={c.id}>
                        {c.name}
                      </option>
                    ))}
                  </select>
                </label>
                <label className="field">
                  <span>Product</span>
                  <select name="productId" value={saleForm.productId} onChange={updateSaleField}>
                    <option value="">Select product</option>
                    {products.map((p) => (
                      <option key={p.id} value={p.id}>
                        {p.productCode}
                      </option>
                    ))}
                  </select>
                </label>
                <label className="field">
                  <span>Quantity</span>
                  <input
                    type="number"
                    step="0.01"
                    name="quantity"
                    value={saleForm.quantity}
                    onChange={updateSaleField}
                  />
                </label>
                <label className="field">
                  <span>Date</span>
                  <input type="date" name="date" value={saleForm.date} onChange={updateSaleField} />
                </label>
              </div>
              <div className="actions">
                <button className="primary" type="submit" disabled={loadingOps}>
                  Record sale
                </button>
              </div>
            </form>
            <div className="list">
              {sales.map((sale) => (
                <div key={sale.id} className="list-item">
                  <div>
                    <p className="strong">
                      {sale.customer?.name ?? '—'} → {sale.productType?.productCode ?? '—'}
                    </p>
                    <p className="muted small">{sale.date}</p>
                  </div>
                  <div className="actions">
                    <span className="pill ghost mono">
                      {sale.quantity} qty · €{sale.cost ?? 0}
                    </span>
                    <button className="ghost danger" type="button" onClick={() => handleSaleDelete(sale.id)}>
                      Delete
                    </button>
                  </div>
                </div>
              ))}
              {!sales.length ? <p className="muted small">No sales logged.</p> : null}
            </div>
          </div>

          <div className="panel-inner stacked">
            <div className="panel-header">
              <div>
                <p className="eyebrow">Returned wax</p>
                <h3>Recycle</h3>
              </div>
              <span className="pill ghost">{returns.length}</span>
            </div>
            <form className="panel-form" onSubmit={handleReturnCreate}>
              <div className="fields two-up">
                <label className="field">
                  <span>Customer</span>
                  <select name="customerId" value={returnForm.customerId} onChange={updateReturnField}>
                    <option value="">Select customer</option>
                    {customers.map((c) => (
                      <option key={c.id} value={c.id}>
                        {c.name}
                      </option>
                    ))}
                  </select>
                </label>
                <label className="field">
                  <span>Material</span>
                  <select name="material" value={returnForm.material} onChange={updateReturnField}>
                    <option>Brown</option>
                    <option>White</option>
                    <option>Pure</option>
                  </select>
                </label>
                <label className="field">
                  <span>Weight (kg)</span>
                  <input
                    type="number"
                    step="0.01"
                    name="weight"
                    value={returnForm.weight}
                    onChange={updateReturnField}
                  />
                </label>
                <label className="field">
                  <span>Date</span>
                  <input type="date" name="returnDate" value={returnForm.returnDate} onChange={updateReturnField} />
                </label>
                <label className="field">
                  <span>Note</span>
                  <input name="note" value={returnForm.note} onChange={updateReturnField} placeholder="Optional" />
                </label>
              </div>
              <div className="actions">
                <button className="primary" type="submit" disabled={loadingOps}>
                  Log return
                </button>
              </div>
            </form>
            <div className="list">
              {returns.map((item) => (
                <div key={item.id} className="list-item">
                  <div>
                    <p className="strong">{item.customer?.name ?? '—'}</p>
                    <p className="muted small">
                      {item.material} · {item.weight} kg · {item.returnDate}
                    </p>
                    {item.note ? <p className="muted small">{item.note}</p> : null}
                  </div>
                  <div className="actions">
                    <span className="pill ghost mono">€{item.totalValue ?? item.value ?? 0}</span>
                    <button className="ghost danger" type="button" onClick={() => handleReturnDelete(item.id)}>
                      Delete
                    </button>
                  </div>
                </div>
              ))}
              {!returns.length ? <p className="muted small">No returns recorded.</p> : null}
            </div>
          </div>
        </section>
        ) : null}
      </div>
      ) : null}
    </div>
  );
}

export default App;
