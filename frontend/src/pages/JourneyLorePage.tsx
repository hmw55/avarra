import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import EntryLayout from '../components/layout/EntryLayout'
import GameScrollPanel from '../components/ui/GameScrollPanel'

type LoreTab = 'continent' | 'history' | 'mana' | 'kingdoms'

function JourneyLorePage() {
  const navigate = useNavigate()
  const [activeTab, setActiveTab] = useState<LoreTab>('continent')

  // Intentional console Easter Egg.
  //
  // TODO: Consider tracking preparation-lore engagement in journey state.
  // Registered journeys could persist this server-side; guest journeys could
  // persist it in the versioned local save.
  //
  // Possible future uses:
  // - NPC acknowledges that the player studied before beginning.
  // - Optional gift, dialogue, trust, or recruitment outcome.
  // - Track individual sections viewed rather than a simple boolean.
  //
  // Do not reward opening this page alone; meaningful engagement should be
  // defined before implementing this.
  useEffect(() => {
    console.info(
      '%cYou came looking for lore. The world may have noticed.',
      'color: #c8ba96; font-weight: bold;',
    )
  }, [])

  return (
    <EntryLayout
      eyebrow="Explore Avarra"
      title="Know the World"
      description="Learn what you wish before beginning your journey."
    >
      <div className="entry-info-box">
        <strong>Knowledge is optional.</strong>

        <p>
          Explore the world at your own pace. You do not need to know
          everything about Avarra before you begin.
        </p>
      </div>

      <button
        type="button"
        className="entry-secondary-action"
        onClick={() => navigate('/journeys/new')}
      >
        Back to Start Game
      </button>

      <div
        className="lore-tabs"
        role="tablist"
        aria-label="Avarra lore sections"
      >
        <button
          type="button"
          className={`lore-tab ${
            activeTab === 'continent' ? 'lore-tab-active' : ''
          }`}
          role="tab"
          aria-selected={activeTab === 'continent'}
          onClick={() => setActiveTab('continent')}
        >
          Continent
        </button>

        <button
          type="button"
          className={`lore-tab ${
            activeTab === 'history' ? 'lore-tab-active' : ''
          }`}
          role="tab"
          aria-selected={activeTab === 'history'}
          onClick={() => setActiveTab('history')}
        >
          History
        </button>

        <button
          type="button"
          className={`lore-tab ${
            activeTab === 'mana' ? 'lore-tab-active' : ''
          }`}
          role="tab"
          aria-selected={activeTab === 'mana'}
          onClick={() => setActiveTab('mana')}
        >
          Mana
        </button>

        <button
          type="button"
          className={`lore-tab ${
            activeTab === 'kingdoms' ? 'lore-tab-active' : ''
          }`}
          role="tab"
          aria-selected={activeTab === 'kingdoms'}
          onClick={() => setActiveTab('kingdoms')}
        >
          Kingdoms
        </button>
      </div>

      <GameScrollPanel className="lore-content lore-scroll-panel">
        {activeTab === 'continent' && (
          <div>
            <h2>Continent</h2>

            {Array.from({ length: 20 }).map((_, index) => (
              <p key={index}>
                Temporary continent lore paragraph {index + 1}.
              </p>
            ))}
          </div>
        )}

        {activeTab === 'history' && (
          <div>
            <h2>History</h2>
            <p>History lore will go here.</p>
          </div>
        )}

        {activeTab === 'mana' && (
          <div>
            <h2>Mana</h2>
            <p>Mana lore will go here.</p>
          </div>
        )}

        {activeTab === 'kingdoms' && (
          <div>
            <h2>Kingdoms</h2>
            <p>Kingdom lore will go here.</p>
          </div>
        )}
      </GameScrollPanel>
    </EntryLayout>
  )
}

export default JourneyLorePage