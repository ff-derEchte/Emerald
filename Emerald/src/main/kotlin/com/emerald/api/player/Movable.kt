package com.emerald.api.player

import com.emerald.api.position.Position

interface Movable : Locatalbe {
    override var position: Position
}