import { useEffect, useMemo, useState } from 'react';
import './App.css';
import {
  addCustomer,
  addProduction,
  addProduct,
  addSale,
  addStorage,
  createWorker,
  deleteLeave,
  deleteCustomer,
  deleteWorker,
  deleteProduct,
  deleteProduction,
  deleteSale,
  deleteStorage,
  editLeave,
  updateCustomer,
  updateProduction,
  updateProduct,
  updateSale,
  editWorker,
  fetchLeavesForWorker,
  fetchProductions,
  fetchProducts,
  fetchSales,
  fetchStorage,
  fetchTodaysLeaves,
  fetchWorkers,
  fetchCustomers,
  requestLeave,
  updateStorageQuantity,
} from './api/client';
import CustomerPanel from './components/CustomerPanel';
import LeaveForm from './components/LeaveForm';
import LeaveTable from './components/LeaveTable';
import ProductPanel from './components/ProductPanel';
import ProductionPanel from './components/ProductionPanel';
import SalesPanel from './components/SalesPanel';
import StoragePanel from './components/StoragePanel';
import WorkerForm from './components/WorkerForm';
import WorkerList from './components/WorkerList';

const TABS = [
  { id: 'team', label: 'Team & leave', sub: 'Workers, absences, staffing' },
  { id: 'products', label: 'Products', sub: 'SKUs, pricing, materials' },
  { id: 'storage', label: 'Storage', sub: 'On-hand inventory' },
  { id: 'production', label: 'Production', sub: 'Daily runs' },
  { id: 'sales', label: 'Sales', sub: 'Orders & cost' },
  { id: 'customers', label: 'Customers', sub: 'Accounts & debt' },
];

function App() {
  const [activeTab, setActiveTab] = useState('team');
  const [workers, setWorkers] = useState([]);
  const [selectedWorkerId, setSelectedWorkerId] = useState(null);
  const [leaves, setLeaves] = useState([]);
  const [todayLeaves, setTodayLeaves] = useState([]);
  const [status, setStatus] = useState(null);
  const [loadingWorkers, setLoadingWorkers] = useState(false);
  const [loadingLeaves, setLoadingLeaves] = useState(false);
  const [products, setProducts] = useState([]);
  const [storageItems, setStorageItems] = useState([]);
  const [productions, setProductions] = useState([]);
  const [sales, setSales] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [loadingProducts, setLoadingProducts] = useState(false);
  const [loadingStorage, setLoadingStorage] = useState(false);
  const [loadingProduction, setLoadingProduction] = useState(false);
  const [loadingSales, setLoadingSales] = useState(false);
  const [loadingCustomers, setLoadingCustomers] = useState(false);

  const selectedWorker = useMemo(
    () => workers.find((worker) => worker.id === selectedWorkerId) || null,
    [workers, selectedWorkerId],
  );

  useEffect(() => {
    refreshWorkers(true);
    refreshTodayLeaves();
    loadProducts();
    loadStorage();
    loadProductions();
    loadSales();
    loadCustomers();
  }, []);

  useEffect(() => {
    if (selectedWorkerId) {
      loadLeaves(selectedWorkerId);
    } else {
      setLeaves([]);
    }
  }, [selectedWorkerId]);

  const pushStatus = (type, message) => setStatus({ type, message, ts: Date.now() });

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

  const loadProducts = async () => {
    setLoadingProducts(true);
    try {
      const data = await fetchProducts();
      setProducts(data);
    } catch (err) {
      setProducts([]);
      pushStatus('error', err.message);
    } finally {
      setLoadingProducts(false);
    }
  };

  const loadStorage = async () => {
    setLoadingStorage(true);
    try {
      const data = await fetchStorage();
      setStorageItems(data);
    } catch (err) {
      setStorageItems([]);
      pushStatus('error', err.message);
    } finally {
      setLoadingStorage(false);
    }
  };

  const loadProductions = async () => {
    setLoadingProduction(true);
    try {
      const data = await fetchProductions();
      setProductions(data);
    } catch (err) {
      setProductions([]);
      pushStatus('error', err.message);
    } finally {
      setLoadingProduction(false);
    }
  };

  const loadSales = async () => {
    setLoadingSales(true);
    try {
      const data = await fetchSales();
      setSales(data);
    } catch (err) {
      setSales([]);
      pushStatus('error', err.message);
    } finally {
      setLoadingSales(false);
    }
  };

  const loadCustomers = async () => {
    setLoadingCustomers(true);
    try {
      const data = await fetchCustomers();
      setCustomers(data);
    } catch (err) {
      setCustomers([]);
      pushStatus('error', err.message);
    } finally {
      setLoadingCustomers(false);
    }
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

  const todayOffCount = todayLeaves.length;
  const availableWorkers = Math.max(workers.length - todayOffCount, 0);
  const totalStock = useMemo(
    () => storageItems.reduce((sum, item) => sum + Number(item.quantity || 0), 0),
    [storageItems],
  );
  const globalLoading =
    loadingWorkers ||
    loadingLeaves ||
    loadingProducts ||
    loadingStorage ||
    loadingProduction ||
    loadingSales ||
    loadingCustomers;

  const handleProductAdd = async (payload) => {
    setLoadingProducts(true);
    try {
      await addProduct(payload);
      pushStatus('success', 'Product saved.');
      await loadProducts();
      await loadStorage();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingProducts(false);
    }
  };

  const handleProductEdit = async (payload) => {
    setLoadingProducts(true);
    try {
      await updateProduct(payload);
      pushStatus('success', 'Product updated.');
      await loadProducts();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingProducts(false);
    }
  };

  const handleProductDelete = async (productId) => {
    if (!window.confirm('Delete this product?')) return;
    setLoadingProducts(true);
    try {
      await deleteProduct(productId);
      pushStatus('success', 'Product removed.');
      await loadProducts();
      await loadStorage();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingProducts(false);
    }
  };

  const handleStorageAdd = async (payload) => {
    setLoadingStorage(true);
    try {
      await addStorage(payload);
      pushStatus('success', 'Storage record created.');
      await loadStorage();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingStorage(false);
    }
  };

  const handleStorageAdjust = async (storageId, quantity) => {
    setLoadingStorage(true);
    try {
      await updateStorageQuantity(storageId, quantity);
      pushStatus('success', 'Quantity updated.');
      await loadStorage();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingStorage(false);
    }
  };

  const handleStorageDelete = async (storageId) => {
    if (!window.confirm('Delete this storage entry?')) return;
    setLoadingStorage(true);
    try {
      await deleteStorage(storageId);
      pushStatus('success', 'Storage entry removed.');
      await loadStorage();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingStorage(false);
    }
  };

  const handleProductionAdd = async (payload) => {
    setLoadingProduction(true);
    try {
      await addProduction(payload);
      pushStatus('success', 'Production added.');
      await loadProductions();
      await loadStorage();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingProduction(false);
    }
  };

  const handleProductionEdit = async (payload) => {
    setLoadingProduction(true);
    try {
      await updateProduction(payload);
      pushStatus('success', 'Production updated.');
      await loadProductions();
      await loadStorage();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingProduction(false);
    }
  };

  const handleProductionDelete = async (productionId) => {
    if (!window.confirm('Delete this production record?')) return;
    setLoadingProduction(true);
    try {
      await deleteProduction(productionId);
      pushStatus('success', 'Production deleted.');
      await loadProductions();
      await loadStorage();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingProduction(false);
    }
  };

  const handleSaleAdd = async (payload) => {
    setLoadingSales(true);
    try {
      await addSale(payload);
      pushStatus('success', 'Sale recorded.');
      await loadSales();
      await loadStorage();
      await loadCustomers();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingSales(false);
    }
  };

  const handleSaleEdit = async (payload) => {
    setLoadingSales(true);
    try {
      await updateSale(payload);
      pushStatus('success', 'Sale updated.');
      await loadSales();
      await loadStorage();
      await loadCustomers();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingSales(false);
    }
  };

  const handleSaleDelete = async (saleId) => {
    if (!window.confirm('Delete this sale?')) return;
    setLoadingSales(true);
    try {
      await deleteSale(saleId);
      pushStatus('success', 'Sale deleted.');
      await loadSales();
      await loadStorage();
      await loadCustomers();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingSales(false);
    }
  };

  const handleCustomerAdd = async (payload) => {
    setLoadingCustomers(true);
    try {
      await addCustomer(payload);
      pushStatus('success', 'Customer added.');
      await loadCustomers();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingCustomers(false);
    }
  };

  const handleCustomerEdit = async (payload) => {
    setLoadingCustomers(true);
    try {
      await updateCustomer(payload);
      pushStatus('success', 'Customer updated.');
      await loadCustomers();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingCustomers(false);
    }
  };

  const handleCustomerDelete = async (customerId) => {
    if (!window.confirm('Delete this customer?')) return;
    setLoadingCustomers(true);
    try {
      await deleteCustomer(customerId);
      pushStatus('success', 'Customer deleted.');
      await loadCustomers();
      await loadSales();
    } catch (err) {
      pushStatus('error', err.message);
    } finally {
      setLoadingCustomers(false);
    }
  };

  const refreshAll = () => {
    refreshWorkers(true);
    refreshTodayLeaves();
    loadProducts();
    loadStorage();
    loadProductions();
    loadSales();
    loadCustomers();
  };

  const teamTab = (
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
  );

  const renderActiveTab = () => {
    switch (activeTab) {
      case 'team':
        return teamTab;
      case 'products':
        return (
          <div className="tab-grid">
            <ProductPanel
              products={products}
              onAdd={handleProductAdd}
              onEdit={handleProductEdit}
              onDelete={handleProductDelete}
              loading={loadingProducts}
            />
          </div>
        );
      case 'storage':
        return (
          <div className="tab-grid">
            <StoragePanel
              storageItems={storageItems}
              products={products}
              onAdd={handleStorageAdd}
              onAdjust={handleStorageAdjust}
              onDelete={handleStorageDelete}
              loading={loadingStorage}
            />
          </div>
        );
      case 'production':
        return (
          <div className="tab-grid">
            <ProductionPanel
              productions={productions}
              products={products}
              onAdd={handleProductionAdd}
              onEdit={handleProductionEdit}
              onDelete={handleProductionDelete}
              loading={loadingProduction}
            />
          </div>
        );
      case 'sales':
        return (
          <div className="tab-grid">
            <SalesPanel
              sales={sales}
              customers={customers}
              products={products}
              onAdd={handleSaleAdd}
              onEdit={handleSaleEdit}
              onDelete={handleSaleDelete}
              loading={loadingSales}
            />
          </div>
        );
      case 'customers':
        return (
          <div className="tab-grid">
            <CustomerPanel
              customers={customers}
              onAdd={handleCustomerAdd}
              onEdit={handleCustomerEdit}
              onDelete={handleCustomerDelete}
              loading={loadingCustomers}
            />
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="app-shell">
      <div className="hero">
        <div>
          <p className="eyebrow">Candle Factory · Ops cockpit</p>
          <h1>Operations in one place</h1>
          <p className="muted">
            Register the team, plan time off, and keep visibility on products, stock, production and
            sales.
          </p>
          <div className="hero-chips">
            <span className="pill">{workers.length} workers</span>
            <span className="pill accent">{todayOffCount} off today</span>
            <span className="pill ghost">{availableWorkers} available</span>
            <span className="pill">{products.length} products</span>
            <span className="pill ghost">{totalStock.toFixed(1)} in stock</span>
          </div>
        </div>
        <div className="hero-actions">
          <button className="ghost" onClick={refreshAll} disabled={globalLoading}>
            Sync everything
          </button>
          <button className="primary" onClick={refreshTodayLeaves} disabled={loadingLeaves}>
            Refresh today
          </button>
        </div>
      </div>

      {status ? (
        <div key={status.ts} className={`status status--${status.type}`}>
          {status.message}
        </div>
      ) : null}

      <div className="tab-bar">
        {TABS.map((tab) => (
          <button
            key={tab.id}
            className={`tab ${activeTab === tab.id ? 'is-active' : ''}`}
            onClick={() => setActiveTab(tab.id)}
          >
            <span className="tab-label">{tab.label}</span>
            <span className="tab-sub">{tab.sub}</span>
          </button>
        ))}
      </div>

      {renderActiveTab()}
    </div>
  );
}

export default App;
