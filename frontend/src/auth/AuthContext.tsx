import { useEffect, useState } from 'react'
import type { ReactNode } from 'react'
import { getCurrentUser } from './authApi'
import { AuthContext } from './authContext'
import type { CurrentUser } from './types'

type AuthProviderProps = {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<CurrentUser | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [isGuest, setIsGuest] = useState(false)

  function continueAsGuest() {
    setUser(null)
    setIsGuest(true)
  }

  useEffect(() => {
    async function loadCurrentUser() {
      try {
        const currentUser = await getCurrentUser()
        setUser(currentUser)
        setIsGuest(false)
      } catch {
        setUser(null)
      } finally {
        setIsLoading(false)
      }
    }

    void loadCurrentUser()
  }, [])

  return (
    <AuthContext.Provider
      value={{
        user,
        isGuest,
        isLoading,
        setUser,
        continueAsGuest,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}