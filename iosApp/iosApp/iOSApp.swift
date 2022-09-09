import SwiftUI
import shared

@main
struct iOSApp: App {
    let sdk = BooksSDK(databaseDriverFactory: DatabaseDriverFactory())
    
	var body: some Scene {
		WindowGroup {
            ContentView(viewModel: .init(sdk: sdk))
		}
	}
}
