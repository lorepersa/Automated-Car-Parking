package itunibo.automatedcarparking.parkingarea

data class IndoorAreaStatus(var reserved : Boolean,
                            var engagedByCar : Boolean,
                            var carEnterTimeoutAlarm : Boolean)