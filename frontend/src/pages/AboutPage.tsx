import EntryLayout from '../components/layout/EntryLayout'

function AboutPage() {
  return (
    <EntryLayout
      eyebrow="About the game"
      title="About Avarra"
      description="A persistent single-player fantasy tabletop RPG where choices leave lasting consequences."
      backTo="/"
      backLabel="Back to Home"
    >
      <p className="entry-tagline">
        The world remembers.
      </p>
    </EntryLayout>
  )
}

export default AboutPage