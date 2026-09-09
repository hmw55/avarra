import { Link } from 'react-router-dom'

function HomePage() {
  return (
    <div className="app">
      <header className="site-header">
        <span className="site-title">AVARRA</span>
      </header>

      <main className="home">
        <section className="intro-panel" aria-labelledby="avarra-title">
          <p className="eyebrow">A persistent fantasy tabletop RPG</p>

          <h1 id="avarra-title">AVARRA</h1>

          <p className="tagline">The world remembers.</p>

          <p className="description">
            Avarra is a persistent single-player fantasy tabletop RPG shaped
            by the choices you make and the paths you leave behind.
          </p>

          <div className="entry-actions">
            <Link to="/enter" className="primary-action">
              Enter Avarra
            </Link>

            <a
              className="github-link"
              href="YOUR_REPO_URL"
              target="_blank"
              rel="noreferrer"
            >
              View on GitHub
            </a>
          </div>
        </section>
      </main>

      <footer className="site-footer">
        <span>Avarra</span>
      </footer>
    </div>
  )
}

export default HomePage