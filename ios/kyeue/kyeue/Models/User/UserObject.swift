//
//  UserObject.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 31.03.2021.
//

import CoreData

@objc(UserObject)
class UserObject: NSManagedObject {
    
    @NSManaged public var key: String
    @NSManaged public var id: String
    @NSManaged public var username: String
    @NSManaged public var firstName: String
    @NSManaged public var lastName: String
    @NSManaged public var created: String
    
}

extension UserObject {
    func toUser() -> SignedUser {
        return SignedUser(key: key, user: User(id: id, username: username, firstName: firstName, lastName: lastName), created: created)
    }
}
