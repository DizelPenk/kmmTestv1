//
//  BookRow.swift
//  iosApp
//
//  Created by Maksom Kolbas on 28.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct BookRow: View {
    var bookRow: Book
    
    var body: some View {
        HStack() {
            VStack(alignment: .leading, spacing: 10.0) {
                Text(bookRow.title)
                Text(bookRow.author)
                Text(bookRow.description_)
            }
            Spacer()
        }
    }
}
