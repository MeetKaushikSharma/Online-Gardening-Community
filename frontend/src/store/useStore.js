import { create } from 'zustand';
import { apiFetch } from '../services/api';

const initialPendingContent = [
  {
    id: 1,
    type: 'tip',
    title: 'Pruning Techniques for Fruit Trees',
    description: 'Learn the best time and methods for pruning fruit trees to maximize yield.',
    author: 'New Gardener',
    photos: []
  }
];

const initialSettings = {
  siteName: 'Gardening Community',
  maintenanceMode: false,
  allowRegistration: true
};

const ensureSuccess = (response, fallbackMessage) => {
  if (!response?.success) {
    throw new Error(response?.message || fallbackMessage);
  }
  return response.data;
};

export const useStore = create((set, get) => ({
  users: [],
  tips: [],
  discussions: [],
  projects: [],
  pendingContent: initialPendingContent,
  settings: initialSettings,

  fetchUsers: async () => {
    const res = await apiFetch('/users');
    const users = ensureSuccess(res, 'Failed to load users') || [];
    set({ users });
  },

  addUser: async (payload) => {
    const res = await apiFetch('/users', {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    const newUser = ensureSuccess(res, 'Failed to add user');
    set((state) => ({ users: [...state.users, newUser] }));
    return newUser;
  },

  updateUser: async (id, payload) => {
    const res = await apiFetch(`/users/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload)
    });
    const updated = ensureSuccess(res, 'Failed to update user');
    set((state) => ({
      users: state.users.map((user) => (user.id === id ? updated : user))
    }));
    return updated;
  },

  deleteUser: async (id) => {
    const res = await apiFetch(`/users/${id}`, { method: 'DELETE' });
    ensureSuccess(res, 'Failed to delete user');
    set((state) => ({
      users: state.users.filter((user) => user.id !== id)
    }));
  },

  fetchTips: async () => {
    const res = await apiFetch('/posts?category=TIP');
    const tips = ensureSuccess(res, 'Failed to load tips') || [];
    set({ tips });
  },

  addTip: async ({ title, description, userId }) => {
    const res = await apiFetch('/posts', {
      method: 'POST',
      body: JSON.stringify({
        userId,
        title,
        content: description,
        category: 'TIP'
      })
    });
    const newTip = ensureSuccess(res, 'Failed to create tip');
    set((state) => ({ tips: [newTip, ...state.tips] }));
    return newTip;
  },

  fetchDiscussions: async () => {
    const res = await apiFetch('/posts?category=DISCUSSION');
    const discussions = ensureSuccess(res, 'Failed to load discussions') || [];
    set({ discussions });
  },

  addDiscussion: async ({ topic, content, userId }) => {
    const res = await apiFetch('/posts', {
      method: 'POST',
      body: JSON.stringify({
        userId,
        title: topic,
        content,
        category: 'DISCUSSION'
      })
    });
    const newDiscussion = ensureSuccess(res, 'Failed to create discussion');
    set((state) => ({ discussions: [newDiscussion, ...state.discussions] }));
    return newDiscussion;
  },

  fetchProjects: async (userId) => {
    const endpoint = userId ? `/projects/user/${userId}` : '/projects';
    const res = await apiFetch(endpoint);
    const projects = ensureSuccess(res, 'Failed to load projects') || [];
    set({ projects });
  },

  addProject: async (project) => {
    const res = await apiFetch('/projects', {
      method: 'POST',
      body: JSON.stringify(project)
    });
    const newProject = ensureSuccess(res, 'Failed to create project');
    set((state) => ({ projects: [newProject, ...state.projects] }));
    return newProject;
  },

  updateProject: async (id, updates) => {
    const res = await apiFetch(`/projects/${id}`, {
      method: 'PUT',
      body: JSON.stringify(updates)
    });
    const updatedProject = ensureSuccess(res, 'Failed to update project');
    set((state) => ({
      projects: state.projects.map((project) =>
        project.id === id ? updatedProject : project
      )
    }));
    return updatedProject;
  },

  approveContent: (id) =>
    set((state) => ({
      pendingContent: state.pendingContent.filter((item) => item.id !== id)
    })),

  updateSettings: (updates) =>
    set((state) => ({
      settings: { ...state.settings, ...updates }
    }))
}));

