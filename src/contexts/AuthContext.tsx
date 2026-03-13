import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import axios from 'axios';

interface User {
  username: string;
  email: string;
}

interface AuthContextType {
  user: User | null;
  login: (username: string, password: string) => Promise<boolean>;
  signup: (username: string, email: string, password: string) => Promise<boolean>;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    const email = localStorage.getItem('email');
    
    if (token && username && email) {
      setUser({ username, email });
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
  }, []);

  const login = async (username: string, password: string): Promise<boolean> => {
    try {
      const response = await axios.post('http://localhost:8081/api/auth/signin', { username, password });
      const { token, email } = response.data;
      
      localStorage.setItem('token', token);
      localStorage.setItem('username', username);
      localStorage.setItem('email', email);
      
      setUser({ username, email });
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      return true;
    } catch (error) {
      return false;
    }
  };

  const signup = async (username: string, email: string, password: string): Promise<boolean> => {
    try {
      const response = await axios.post('http://localhost:8081/api/auth/signup', { username, email, password });
      const { token } = response.data;
      
      localStorage.setItem('token', token);
      localStorage.setItem('username', username);
      localStorage.setItem('email', email);
      
      setUser({ username, email });
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      return true;
    } catch (error) {
      return false;
    }
  };

  const logout = () => {
    localStorage.clear();
    setUser(null);
    delete axios.defaults.headers.common['Authorization'];
  };

  return (
    <AuthContext.Provider value={{ user, login, signup, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
};