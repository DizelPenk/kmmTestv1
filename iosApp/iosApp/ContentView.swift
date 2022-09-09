import SwiftUI
import shared

struct ContentView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    
	var body: some View {
        listView()
	}
    
    private func listView() -> AnyView {
        switch viewModel.books {
        case .loading:
            return AnyView(Text("Loading...").multilineTextAlignment(.center))
        case .result(let books):
            return AnyView(List(books) { book in
                BookRow(bookRow: book)
            })
        case .error(let description):
            return AnyView(Text(description).multilineTextAlignment(.center))
        }
    }
}

extension ContentView {

    enum LoadableBooks {
        case loading
        case result([Book])
        case error(String)
    }
    
    class ViewModel: ObservableObject {
        let sdk: BooksSDK
        @Published var books = LoadableBooks.loading
        
        init(sdk: BooksSDK) {
            self.sdk = sdk
            DispatchQueue.main.async {
                self.loadBooks(page: 3)
            }
        }
        
        func loadBooks(page: Int32) {
            self.books = .loading
            do {
                if (try sdk.getBooksFromDatabase().isEmpty) {
                    sdk.getBooksFromApi(page: page, completionHandler: { books, error in
                        if let books = books {
                            self.books = .result(books)
                        } else {
                            self.books = .error(error?.localizedDescription ?? "error")
                        }
                    })
                } else {
                    try self.books = .result(sdk.getBooksFromDatabase())
                }
            } catch {
                //todo
            }
           
            
        }
    }
}

extension Book: Identifiable { }
