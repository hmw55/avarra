import { apiFetch } from '../api/http'

export type JourneySummary = {
  id: string
  name: string | null
  createdAt: string
  updatedAt: string
}

export async function getJourneys(): Promise<JourneySummary[]> {
  const response = await apiFetch('/api/journeys')

  if (!response.ok) {
    throw new Error(`Journey request failed: ${response.status}`)
  }

  return response.json()
}