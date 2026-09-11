import { useEffect, useRef, useState } from 'react'
import type { MouseEvent as ReactMouseEvent, ReactNode } from 'react'

type GameScrollPanelProps = {
  children: ReactNode
  className?: string
}

function GameScrollPanel({
  children,
  className = '',
}: GameScrollPanelProps) {
  const contentRef = useRef<HTMLDivElement>(null)
  const [thumbHeight, setThumbHeight] = useState(0)
  const [thumbTop, setThumbTop] = useState(0)

  function startThumbDrag(event: ReactMouseEvent<HTMLDivElement>) {
    event.preventDefault()

    const content = contentRef.current

    if (!content) {
      return
    }

    const startY = event.clientY
    const startScrollTop = content.scrollTop

    const maxScrollTop = content.scrollHeight - content.clientHeight
    const maxThumbTop = content.clientHeight - thumbHeight

    if (maxScrollTop <= 0 || maxThumbTop <= 0) {
      return
    }

    function handleMouseMove(moveEvent: MouseEvent) {
      const deltaY = moveEvent.clientY - startY
      const scrollDelta = (deltaY / maxThumbTop) * maxScrollTop

      contentRef.current!.scrollTop = startScrollTop + scrollDelta
    }

    function handleMouseUp() {
      window.removeEventListener('mousemove', handleMouseMove)
      window.removeEventListener('mouseup', handleMouseUp)
    }

    window.addEventListener('mousemove', handleMouseMove)
    window.addEventListener('mouseup', handleMouseUp)
  }

  function updateThumb() {
    const content = contentRef.current

    if (!content) {
      return
    }

    const { clientHeight, scrollHeight, scrollTop } = content

    if (scrollHeight <= clientHeight) {
      setThumbHeight(clientHeight)
      setThumbTop(0)
      return
    }

    const height = Math.max(
      24,
      (clientHeight / scrollHeight) * clientHeight,
    )

    const maxThumbTop = clientHeight - height
    const maxScrollTop = scrollHeight - clientHeight
    const top = (scrollTop / maxScrollTop) * maxThumbTop

    setThumbHeight(height)
    setThumbTop(top)
  }

  function scrollUp() {
    contentRef.current?.scrollBy({
      top: -80,
      behavior: 'smooth',
    })
  }

  function scrollDown() {
    contentRef.current?.scrollBy({
      top: 80,
      behavior: 'smooth',
    })
  }

  useEffect(() => {
    updateThumb()

    const content = contentRef.current

    if (!content) {
      return
    }

    content.addEventListener('scroll', updateThumb)
    window.addEventListener('resize', updateThumb)

    return () => {
      content.removeEventListener('scroll', updateThumb)
      window.removeEventListener('resize', updateThumb)
    }
  }, [children])

  return (
    <div className={`game-scroll-panel ${className}`}>
      <div
        ref={contentRef}
        className="game-scroll-content"
      >
        {children}
      </div>

      <div className="game-scroll-controls">
        <button
          type="button"
          className="game-scroll-arrow"
          onClick={scrollUp}
          aria-label="Scroll up"
        >
          ▲
        </button>

        <div className="game-scroll-track">
          <div
            className="game-scroll-thumb"
            onMouseDown={startThumbDrag}
            style={{
              height: `${thumbHeight}px`,
              transform: `translateY(${thumbTop}px)`,
            }}
          />
        </div>

        <button
          type="button"
          className="game-scroll-arrow"
          onClick={scrollDown}
          aria-label="Scroll down"
        >
          ▼
        </button>
      </div>
    </div>
  )
}

export default GameScrollPanel