package it.unibo.automatedcarparking.sprint1.gui

import it.unibo.automatedcarparking.sprint1.gui.status.TransportTrolleyStatus

interface ITransportTrolleyObserver {

    fun update(status : TransportTrolleyStatus)
}