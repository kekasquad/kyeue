//
//  UsersStorageManager.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import UIKit
import CoreData

protocol UsersDataManager {
    
    func saveUser(user: User, completion: @escaping () -> ())
    
    func getUser() -> User?
    
    func deleteUser(completion: @escaping () -> ())
    
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

    
    func saveUser(user: User, completion: @escaping () -> ()) {

        container.performBackgroundTask { (context) in
            
            guard let allUsers = try? context.fetch(UserObject.fetchRequest) else { return }
            
            for userInContext in allUsers {
                context.delete(userInContext)
            }
            
            let currentUserObject = NSEntityDescription.insertNewObject(forEntityName: "UserObject", into: context) as? UserObject
            
            currentUserObject?.id = user.id
            currentUserObject?.firstName = user.firstName
            currentUserObject?.lastName = user.lastName
            currentUserObject?.username = user.username
            
            
            try? context.save()
            
            DispatchQueue.main.async {
                completion()
            }
        }
    }
    
    func getUser() -> User? {
        
        guard let allUsers = try? container.viewContext.fetch(UserObject.fetchRequest) else { return nil }
            
        return allUsers.first?.toUser()
    }
    
    func deleteUser(completion: @escaping () -> ()) {
        
        container.performBackgroundTask { (context) in
            
            guard let allUsers = try? context.fetch(UserObject.fetchRequest) else { return }
            
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
