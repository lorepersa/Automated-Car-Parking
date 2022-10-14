package itunibo.automatedcarparking.transporttrolley

data class TransportTrolleyConfiguration(val timeLimitBackToHome : Long,
                                         val updatePositionAfterEachStep : Boolean,
                                         val stepMoveDuration : Long)