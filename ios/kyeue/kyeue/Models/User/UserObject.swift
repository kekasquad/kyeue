//
//  UserObject.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import CoreData

@objc(UserObject)
class UserObject: NSManagedObject {
    
    @NSManaged public var id: UUID
    @NSManaged public var username: String
    @NSManaged public var firstName: String
    @NSManaged public var lastName: String
    

    @nonobjc public class var fetchRequest: NSFetchRequest<UserObject> {
        return NSFetchRequest<UserObject>(entityName: "UserObject");
    }
}

extension UserObject{
    func toUser() -> User {
        return User(id: id, username: username, firstName: firstName, lastName: lastName)
    }
}
