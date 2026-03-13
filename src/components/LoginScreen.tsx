import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { Button, Input } from '@nextui-org/react';

export const LoginScreen: React.FC = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  const { login, signup } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      let success = false;
      
      if (isLogin) {
        success = await login(formData.username, formData.password);
      } else {
        success = await signup(formData.username, formData.email, formData.password);
      }

      if (!success) {
        setError(isLogin ? 'Invalid credentials' : 'Registration failed');
      }
    } catch (error) {
      setError('Something went wrong');
    } finally {
      setLoading(false);
    }
  };

return (
  <div className="fixed inset-0 bg-gray-900 flex items-center justify-center p-4">
    <div className="w-full max-w-md space-y-6">
      {/* Header */}
      <div className="text-center">
        <h1 className="text-3xl font-bold text-white mb-2">Task Manager</h1>
        <p className="text-gray-300">
          {isLogin ? 'Sign in to your account' : 'Create your account'}
        </p>
      </div>
      
      {/* Form */}
      <div className="bg-gray-800 p-6 rounded-xl shadow-lg border border-gray-700">
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Username"
            placeholder="Enter your username"
            value={formData.username}
            onChange={(e) => setFormData({...formData, username: e.target.value})}
            required
            variant="bordered"
            classNames={{
              input: "bg-gray-700 text-white placeholder-gray-400",
              inputWrapper: "border-gray-600 hover:border-blue-500 bg-gray-700",
              label: "text-gray-300"
            }}
          />
          
          {!isLogin && (
            <Input
              label="Email"
              type="email"
              placeholder="Enter your email"
              value={formData.email}
              onChange={(e) => setFormData({...formData, email: e.target.value})}
              required
              variant="bordered"
              classNames={{
                input: "bg-gray-700 text-white placeholder-gray-400",
                inputWrapper: "border-gray-600 hover:border-blue-500 bg-gray-700",
                label: "text-gray-300"
              }}
            />
          )}
          
          <Input
            label="Password"
            type="password"
            placeholder="Enter your password"
            value={formData.password}
            onChange={(e) => setFormData({...formData, password: e.target.value})}
            required
            variant="bordered"
            classNames={{
              input: "bg-gray-700 text-white placeholder-gray-400",
              inputWrapper: "border-gray-600 hover:border-blue-500 bg-gray-700",
              label: "text-gray-300"
            }}
          />

          {error && (
            <p className="text-red-400 text-sm text-center bg-red-900/30 p-2 rounded border border-red-800">{error}</p>
          )}

          <Button
            type="submit"
            color="primary"
            className="w-full font-medium bg-blue-600 hover:bg-blue-700"
            isLoading={loading}
            size="lg"
          >
            {isLogin ? 'Sign In' : 'Sign Up'}
          </Button>
          
          <Button
            variant="light"
            className="w-full text-blue-400 hover:text-blue-300"
            onClick={() => setIsLogin(!isLogin)}
            size="sm"
          >
            {isLogin ? 'Need an account? Sign up' : 'Already have an account? Sign in'}
          </Button>
        </form>
      </div>
    </div>
  </div>
);
};