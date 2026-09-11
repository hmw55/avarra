import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import EntryLayout from '../components/layout/EntryLayout'
import { getJourneys } from '../journey/journeyApi'
import type { JourneySummary } from '../journey/journeyApi'
import { useAuth } from '../auth/useAuth'
import { hasGuestJourney } from '../journey/guestJourneyStorage'

/**
 * TODO: Replace the temporary journey actions with save-aware entry states.
 *
 * Future behavior:
 * - Hide "Continue Game" when no save exists.
 * - Registered users may create and manage multiple saves.
 * - Guest users may only have one active save:
 *   - Hide "Start New Game" once a guest save exists.
 *   - Guests cannot create an additional save.
 *   - Guests cannot delete/reset their save through the application;
 *     clearing local browser data will remain the manual reset path unless
 *     guest save management is intentionally added later.
 * - Use authentication state to explain guest save limitations before a
 *   guest begins their first journey.
 */

/**
 * Presents the journey entry point before character preparation and gameplay.
 *
 * Save-aware controls will eventually differ between registered and guest
 * players. This page remains part of the shared pre-game interface.
 */
function GameEntryPage() {

  const { user, isGuest, isLoading } = useAuth()
  const navigate = useNavigate()
  const [journeys, setJourneys] = useState<JourneySummary[]>([])
  const guestHasJourney = isGuest && hasGuestJourney() 

  // Intentional console easter egg for travelers approaching their journey.
  useEffect(() => {
    console.info(
      '%cYou made it this far. Surely nothing life-altering could happen next.',
      'color: #c8ba96; font-weight: bold;',
    )
  }, [])

  useEffect(() => {
    if (!user) {
      return
    }

    async function loadJourneys() {
      const currentJourneys = await getJourneys()
      setJourneys(currentJourneys)
    }

    void loadJourneys()
  }, [user])

  if (isLoading) {
    return null
  }

  const hasJourney = journeys.length > 0

  return (
    <EntryLayout
      eyebrow="Your journey"
      title="Avarra Awaits"
      description="Choose how you would like to continue."
    >
      <div className="entry-info-box">
        <strong>You do not need to study Avarra before you begin.</strong>

        <p>
          Starting a new journey will take you to a preparation screen where
          you can explore the lore, history, peoples, and foundations of
          Avarra before choosing your character.
        </p>

        <p>
          You can read as much or as little as you want before continuing.
          You'll have the ability to read during gameplay as well.
        </p>
      </div>

      <div className="entry-actions">
        <button
          type="button"
          className="entry-primary-action"
          onClick={() => navigate('/journeys/new')}
        >
          {guestHasJourney
            ? 'Continue Game'
            : isGuest
                ? 'Start Game'
                : 'Start New Game'}
        </button>

        {hasJourney && (
          <button
            type="button"
            className="entry-secondary-action"
          >
            Continue Game
          </button>
        )}
      </div>
    </EntryLayout>
  )
}

export default GameEntryPage