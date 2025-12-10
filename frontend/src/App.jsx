import { useEffect, useMemo, useState } from 'react';
import './App.css';
import {
  createWorker,
  deleteLeave,
  deleteWorker,
  editLeave,
  editWorker,
  fetchLeavesForWorker,
  fetchTodaysLeaves,
  fetchWorkers,
  requestLeave,
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

  const selectedWorker = useMemo(
    () => workers.find((worker) => worker.id === selectedWorkerId) || null,
    [workers, selectedWorkerId],
  );

  useEffect(() => {
    refreshWorkers(true);
    refreshTodayLeaves();
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
    </div>
  );
}

export default App;
