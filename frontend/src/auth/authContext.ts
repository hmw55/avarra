import { createContext } from 'react'
import type { CurrentUser } from './types'

export type AuthContextValue = {
  user: CurrentUser | null
  isGuest: boolean
  isLoading: boolean
  setUser: (user: CurrentUser | null) => void
  continueAsGuest: () => void
}
export const AuthContext = createContext<AuthContextValue | undefined>(
  undefined,
)