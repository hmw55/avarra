export type GuestJourney = {
  version: 1
  id: string
  createdAt: string
  updatedAt: string
}

const GUEST_JOURNEY_STORAGE_KEY = 'avarra.guestJourney'

export function getGuestJourney(): GuestJourney | null {
  const storedJourney = localStorage.getItem(GUEST_JOURNEY_STORAGE_KEY)

  if (!storedJourney) {
    return null
  }

  return JSON.parse(storedJourney) as GuestJourney
}

export function hasGuestJourney(): boolean {
  return getGuestJourney() !== null
}