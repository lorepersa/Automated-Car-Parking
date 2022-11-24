package it.unibo.automatedcarparking.parkservicestatusgui

import it.unibo.automatedcarparking.parkservicestatusgui.status.AutomatedCarParkingStatus

interface IAutomatedCarParkingStatusObserver {
    fun update(status : AutomatedCarParkingStatus)
}