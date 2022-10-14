package it.unibo.transporttrolleygui

import it.unibo.transporttrolleygui.status.TransportTrolleyStatus

interface ITransportTrolleyObserver {

    fun update(status : TransportTrolleyStatus)
}