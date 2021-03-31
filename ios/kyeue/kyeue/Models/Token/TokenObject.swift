//
//  TokenObject.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 31.03.2021.
//

import CoreData

@objc(TokenObject)
class TokenObject: NSManagedObject {
    
    @NSManaged public var refresh: String
    @NSManaged public var access: String
    
}

extension TokenObject{
    func toToken() -> Token {
        return Token(refresh: refresh, access: access)
    }
}
