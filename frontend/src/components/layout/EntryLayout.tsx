import type { ReactNode } from 'react'
import { Link } from 'react-router-dom'
import './EntryLayout.css'

import EntryNav from '../navigation/EntryNav'

/**
 * Content and navigation options for the shared pre-game Avarra layout.
 *
 * Page-specific content is provided through `children`, while the layout owns
 * the common framing, branding, background, and navigation presentation.
 */
type EntryLayoutProps = {
  eyebrow?: string
  title: string
  description?: string
  children?: ReactNode
  backTo?: string
  backLabel?: string
}

/**
 * Shared presentation shell for Avarra's pre-game screens.
 *
 * This layout provides the consistent old-school fantasy game interface used
 * by entry, authentication, game-selection, lore, and character-selection
 * screens. Gameplay itself is intentionally excluded because it will use a
 * separate interface designed around the game table and active play.
 *
 * Decorative frame elements are hidden from assistive technologies because
 * they provide no semantic content.
 */
function EntryLayout({
  eyebrow,
  title,
  description,
  children,
  backTo,
  backLabel = 'Back',
}: EntryLayoutProps) {
  return (
    <div className="entry-layout">
      <EntryNav />

      <main className="entry-main">
        <section className="entry-panel">

          <span
            className="entry-frame-corner entry-frame-corner-top-left"
            aria-hidden="true"
          />
          <span
            className="entry-frame-corner entry-frame-corner-top-right"
            aria-hidden="true"
          />
          <span
            className="entry-frame-corner entry-frame-corner-bottom-left"
            aria-hidden="true"
          />
          <span
            className="entry-frame-corner entry-frame-corner-bottom-right"
            aria-hidden="true"
          />

          {eyebrow && (
            <p className="entry-eyebrow">
              {eyebrow}
            </p>
          )}

          <h1 className="entry-title">
            {title}
          </h1>

          {description && (
            <p className="entry-description">
              {description}
            </p>
          )}

          <div className="entry-content">
            {children}
          </div>

          {backTo && (
            <Link to={backTo} className="entry-back-link">
              ← {backLabel}
            </Link>
          )}
        </section>
      </main>

      <footer className="entry-footer">
        <span>The world remembers.</span>
      </footer>
    </div>
  )
}

export default EntryLayout