import { createContext, useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiFetch } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUser = localStorage.getItem('gardenUser');
    if (storedUser) {
      try {
        const parsedUser = JSON.parse(storedUser);
        setUser(parsedUser);
      } catch (error) {
        localStorage.removeItem('gardenUser');
      }
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    if (loading || !user) return;
    const role = (user.role || '').toUpperCase();
    if (role === 'ADMIN') {
      navigate('/admin/dashboard');
    } else {
      navigate('/gardener/dashboard');
    }
  }, [user, loading, navigate]);

  const login = async (email, password, rememberMe = false) => {
    try {
      const res = await apiFetch('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password })
      });

      if (!res?.success) {
        return { success: false, error: res?.message || 'Login failed' };
      }

      const userData = res.data;
      setUser(userData);
      if (rememberMe) {
        localStorage.setItem('gardenUser', JSON.stringify(userData));
      } else {
        localStorage.removeItem('gardenUser');
      }

      // Handle role case-insensitively
      const role = (userData.role || '').toUpperCase();
      if (role === 'ADMIN') {
        navigate('/admin/dashboard');
      } else {
        navigate('/gardener/dashboard');
      }

      return { success: true };
    } catch (error) {
      return { success: false, error: error.message || 'Login failed' };
    }
  };

  const register = async (username, email, password) => {
    try {
      const res = await apiFetch('/auth/register', {
        method: 'POST',
        body: JSON.stringify({ username, email, password })
      });
      if (!res?.success) {
        return { success: false, error: res?.message || 'Registration failed' };
      }
      return { success: true, message: res.message || 'Registration successful! Please login.' };
    } catch (error) {
      return { success: false, error: error.message || 'Registration failed' };
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('gardenUser');
    navigate('/login');
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, register, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

export { useAuth };
