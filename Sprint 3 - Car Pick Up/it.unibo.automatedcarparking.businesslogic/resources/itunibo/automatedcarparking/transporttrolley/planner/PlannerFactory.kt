package itunibo.automatedcarparking.transporttrolley.planner

object PlannerFactory {
	
	fun create() : IPlanner {
		SingletonPlanner.init()
		return SingletonPlanner
	}
}