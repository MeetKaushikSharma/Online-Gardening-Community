import { useEffect, useState } from 'react';
import { useStore } from '../../store/useStore';
import { useForm } from 'react-hook-form';

const UserManagement = () => {
  const { users, fetchUsers, addUser, updateUser, deleteUser } = useStore();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const { register, handleSubmit, reset } = useForm();

  useEffect(() => {
    fetchUsers().catch((error) => console.error('Failed to load users', error));
  }, [fetchUsers]);

  const openModal = (user = null) => {
    setEditingUser(user);
    reset({
      username: user?.username || '',
      email: user?.email || '',
      role: user?.role || 'GARDENER',
      password: ''
    });
    setIsModalOpen(true);
  };

  const onSubmit = async (data) => {
    setSubmitting(true);
    const payload = {
      username: data.username,
      email: data.email,
      role: data.role,
      password: data.password
    };

    try {
      if (editingUser) {
        await updateUser(editingUser.id, payload);
        alert('User updated successfully!');
      } else {
        await addUser(payload);
        alert('User created successfully!');
      }
      setIsModalOpen(false);
      reset();
      setEditingUser(null);
    } catch (error) {
      alert(error.message || 'Unable to save user');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this user?')) return;
    try {
      await deleteUser(id);
      alert('User deleted successfully!');
    } catch (error) {
      alert(error.message || 'Unable to delete user');
    }
  };

  return (
    <div>
      <div className="mb-4 flex justify-between items-center">
        <h2 className="text-xl font-semibold text-foreground">User Accounts</h2>
        <button
          onClick={() => openModal()}
          className="btn btn-primary btn-accent-green px-4 py-2 rounded"
        >
          Add New User
        </button>
      </div>

      <div className="glass-card overflow-hidden">
        <table className="min-w-full">
          <thead className="bg-primary-green/10">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-foreground uppercase">Username</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-foreground uppercase">Email</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-foreground uppercase">Role</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-foreground uppercase">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-border">
            {users.map((user) => (
              <tr key={user.id}>
                <td className="px-6 py-4 whitespace-nowrap text-foreground">{user.username}</td>
                <td className="px-6 py-4 whitespace-nowrap text-muted">{user.email}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 text-xs rounded-full ${
                    user.role === 'ADMIN'
                      ? 'bg-accent-coral/20 text-accent-coral'
                      : 'bg-primary-light/20 text-primary-green'
                  }`}>
                    {user.role}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap space-x-2 text-sm">
                  <button
                    onClick={() => openModal(user)}
                    className="btn-primary btn-sm"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(user.id)}
                    className="btn-danger btn-sm"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="glass-card p-6 w-full max-w-md">
            <h3 className="text-xl font-semibold mb-4 text-foreground">
              {editingUser ? 'Edit User' : 'Add New User'}
            </h3>
            <form onSubmit={handleSubmit(onSubmit)}>
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-foreground">Username</label>
                  <input
                    {...register('username', { required: true })}
                    className="form-input mt-1"
                    placeholder="Enter username"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-foreground">Email</label>
                  <input
                    type="email"
                    {...register('email', { required: true })}
                    className="form-input mt-1"
                    placeholder="Enter email"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-foreground">Password</label>
                  <input
                    type="password"
                    {...register('password')}
                    className="form-input mt-1"
                    placeholder={editingUser ? 'Leave blank to keep current password' : 'Set a password'}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-foreground">Role</label>
                  <select
                    {...register('role', { required: true })}
                    className="form-select mt-1"
                  >
                    <option value="GARDENER">Gardener</option>
                    <option value="ADMIN">Admin</option>
                  </select>
                </div>
              </div>
              <div className="mt-6 flex justify-end space-x-2">
                <button
                  type="button"
                  onClick={() => {
                    setIsModalOpen(false);
                    reset();
                    setEditingUser(null);
                  }}
                  className="btn-secondary"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={submitting}
                  className="btn-primary btn-accent-green disabled:opacity-50"
                >
                  {editingUser ? 'Update' : 'Create'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserManagement;
