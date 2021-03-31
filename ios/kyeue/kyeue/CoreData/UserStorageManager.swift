//
//  UserStorageManager.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 31.03.2021.
//

import UIKit
import CoreData

protocol UsersDataManager {
    
    func save(user: SignedUser, completion: @escaping () -> ())
    
    func get() -> SignedUser?
    
    func delete(completion: @escaping () -> ())
    
}

class UsersStorageManager: UsersDataManager {
    
    public static let shared: UsersDataManager = UsersStorageManager() // создаем Синглтон
    
    private init(){}
    
    private lazy var container: NSPersistentContainer = {
        
        let appDelegate = UIApplication.shared.delegate as? AppDelegate
        
        if let appDelegate = appDelegate {
            return appDelegate.persistentContainer
        }
        
        return NSPersistentContainer()
    }()
    
    private var fetch = NSFetchRequest<UserObject>(entityName: "UserObject")

    
    func save(user: SignedUser, completion: @escaping () -> ()) {

        container.performBackgroundTask { (context) in
            
            guard let allUsers = try? context.fetch(self.fetch) else { return }
            
            for userInContext in allUsers {
                context.delete(userInContext)
            }
            
            let currentUserObject = NSEntityDescription.insertNewObject(forEntityName: "UserObject", into: context) as? UserObject
            
            currentUserObject?.key = user.key
            currentUserObject?.created = user.created
            currentUserObject?.id = user.user.id
            currentUserObject?.firstName = user.user.firstName
            currentUserObject?.lastName = user.user.lastName
            currentUserObject?.username = user.user.username
            
            try? context.save()
            
            DispatchQueue.main.async {
                completion()
            }
        }
    }
    
    func get() -> SignedUser? {
        
        guard let allUsers = try? container.viewContext.fetch(fetch) else { return nil }
            
        return allUsers.first?.toUser()
    }
    
    func delete(completion: @escaping () -> ()) {
        
        container.performBackgroundTask { (context) in
            
            guard let allUsers = try? context.fetch(self.fetch) else { return }
            
            for userInContext in allUsers {
                context.delete(userInContext)
            }
            
            try? context.save()
            
            DispatchQueue.main.async {
                completion()
            }
        }
    }
    
    
    
}
