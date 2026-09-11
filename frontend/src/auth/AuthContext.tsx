import { useEffect, useState } from 'react'
import type { ReactNode } from 'react'
import { getCurrentUser } from './authApi'
import { AuthContext } from './authContext'
import type { CurrentUser } from './types'

type AuthProviderProps = {
  children: ReactNode
}

const GUEST_MODE_STORAGE_KEY = 'avarra.guestMode'

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<CurrentUser | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [isGuest, setIsGuest] = useState(false)

  function continueAsGuest() {
    sessionStorage.setItem(GUEST_MODE_STORAGE_KEY, 'true')
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
        setIsGuest(sessionStorage.getItem(GUEST_MODE_STORAGE_KEY) === 'true')
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