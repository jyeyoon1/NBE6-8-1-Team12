"use client";

import {
  createContext,
  useContext,
  useState,
  useEffect,
  ReactNode,
} from "react";

interface AuthContextType {
  isAuthenticated: boolean;
  isLoading: boolean;
  isAdmin: boolean;
  setIsAuthenticated: (value: boolean) => void;
  setIsAdmin: (value: boolean) => void;
  checkAuthStatus: () => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [isAdmin, setIsAdmin] = useState(false);
  const BACKEND_URL = "http://localhost:8080";

  const checkAuthStatus = async () => {
    try {
      const res = await fetch(`${BACKEND_URL}/api/member/status`, {
        method: "GET",
        credentials: "include",
      });
      const data = await res.json();
      console.log('Auth status response:', data);
      setIsAuthenticated(data.authenticated === true);
      setIsAdmin(data.role === 'ADMIN' || data.isAdmin === true);
    } catch (e) {
      console.error(e);
      setIsAuthenticated(false);
      setIsAdmin(false);
    } finally {
      setIsLoading(false);
    }
  };

  const logout = async () => {
    try {
      const res = await fetch(`${BACKEND_URL}/api/member/logout`, {
        method: "POST",
        credentials: "include",
      });
      if (res.ok) {
        setIsAuthenticated(false);
        window.location.href = "/member/login";
      }
    } catch (e) {
      console.error("로그아웃 실패", e);
    }
  };

  useEffect(() => {
    checkAuthStatus();
  }, []);

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        isLoading,
        isAdmin,
        setIsAuthenticated,
        setIsAdmin,
        checkAuthStatus,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
