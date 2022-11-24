package itunibo.automatedcarparking.parkingarea

data class OutdoorAreaStatus(var reserved : Boolean,
                             var engagedByCar : Boolean,
                             var dtfreeTimeoutAlarm : Boolean)