import { useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import EntryLayout from '../components/layout/EntryLayout'
import { useAuth } from '../auth/useAuth'

function EnterPage() {
  const navigate = useNavigate()
  const { continueAsGuest } = useAuth()

  // Intentional console easter agg for curious travelers.
  useEffect(() => {
    console.info(
      '%cYou found the hidden path. The world remembers curious travelers.',
      'color: #c8ba96; font-weight: bold;'
    )
  }, [])

  function handleContinueAsGuest() {
    continueAsGuest()
    navigate('/journeys')
  }

  return (
    <EntryLayout
      eyebrow="Enter the world"
      title="Enter Avarra"
      description="Choose how you would like to continue."
      backTo="/"
      backLabel="Back to Home"
    >
      <div className="entry-actions">
        <Link
          to="/login"
          className="entry-primary-action"
        >
          Log In
        </Link>

        <Link
          to="/register"
          className="entry-secondary-action"
        >
          Create Account
        </Link>

        <button
          type="button"
          className="entry-secondary-action"
          onClick={handleContinueAsGuest}
        >
          Continue as Guest
        </button>
      </div>
    </EntryLayout>
  )
}

export default EnterPage