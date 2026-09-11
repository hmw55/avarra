import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import EntryLayout from '../components/layout/EntryLayout'

function NewJourneyPage() {

  const navigate = useNavigate()

  // Intentional console Easter Egg.
  useEffect(() => {
    console.info(
      '%cPreparation screen reached. Confidence remains optional.',
      'color: #c8ba96; font-weight: bold;',
    )
  }, [])

  return (
    <EntryLayout
      eyebrow="Prepare your journey"
      title="Before You Begin"
      description="Explore what you need, then choose who you will become."
    >
      <div className="entry-info-box">
        <strong>You can learn as much or as little as you want.</strong>

        <p>
          Review Avarra's history, peoples, and foundations before choosing
          your character.
        </p>
      </div>

      <div className="entry-actions">
        <button
          type="button"
          className="entry-secondary-action"
          onClick={() => navigate(`/journeys/new/lore`)}
        >
          Explore Avarra
        </button>

        <button
          type="button"
          className="entry-primary-action"
        >
          Choose Character
        </button>
      </div>
    </EntryLayout>
  )
}

export default NewJourneyPage