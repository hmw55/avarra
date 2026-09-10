import { useEffect } from 'react'
import { Link } from 'react-router-dom'
import EntryLayout from '../components/layout/EntryLayout'

/**
 * Landing page for Avarra.
 *
 * Introduces the game's core premise and provides the primary entry point
 * into the pre-game experience.
 */
function HomePage() {
  // Intentional console easter egg for curious travelers.
  useEffect(() => {
    console.info(
      '%cYou are one step closer to the Source. Please do not lick the mana.',
      'color: #c8ba96; font-weight: bold;',
    )
  }, [])

  return (
    <EntryLayout
      eyebrow="A persistent fantasy tabletop RPG"
      title="AVARRA"
      description="Avarra is a persistent single-player fantasy tabletop RPG shaped by the choices you make and the paths you leave behind."
    >
      <p className="entry-tagline">
        The world remembers.
      </p>

      <div className="entry-actions">
        <Link
          to="/enter"
          className="entry-primary-action"
        >
          Enter Avarra
        </Link>
      </div>
    </EntryLayout>
  )
}

export default HomePage