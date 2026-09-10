import { apiFetch } from "../api/http"

import type {
  CurrentUser,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
} from './types'

export type CsrfToken = {
  token: string
  headerName: string
  parameterName: string
}

export async function getCsrfToken(): Promise<CsrfToken> {
  const response = await apiFetch("/api/auth/csrf")

  if (!response.ok) {
    throw new Error(`Failed to fetch CSRF token: ${response.status}`)
  }

  return response.json()
}

export async function login(
  request: LoginRequest,
): Promise<LoginResponse> {
  const csrf = await getCsrfToken()

  const response = await apiFetch('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [csrf.headerName]: csrf.token,
    },
    body: JSON.stringify(request),
  })

  if (!response.ok) {
    throw new Error(`Login failed: ${response.status}`)
  }

  return response.json()
}

export async function register(
  request: RegisterRequest,
): Promise<RegisterResponse> {
  const csrf = await getCsrfToken()

  const response = await apiFetch('/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [csrf.headerName]: csrf.token,
    },
    body: JSON.stringify(request),
  })

  if (!response.ok) {
    throw new Error(`Registration failed: ${response.status}`)
  }

  return response.json()
}

export async function getCurrentUser(): Promise<CurrentUser> {
  const response = await apiFetch('/api/auth/me')

  if (!response.ok) {
    throw new Error(`Current user request failed: ${response.status}`)
  }

  return response.json()
}

export async function logout(): Promise<void> {
  const csrf = await getCsrfToken()

  const response = await apiFetch('/api/auth/logout', {
    method: 'POST',
    headers: {
      [csrf.headerName]: csrf.token,
    },
  })

  if (!response.ok) {
    throw new Error(`Logout failed: ${response.status}`)
  }
}