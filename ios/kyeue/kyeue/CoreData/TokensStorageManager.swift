//
//  TokensStorageManager.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 31.03.2021.
//

import UIKit
import CoreData

protocol TokensDataManager {
    
    func save(token: Token, completion: @escaping () -> ())
    
    func get() -> Token?
    
    func delete(completion: @escaping () -> ())
    
}

class TokensStorageManager: TokensDataManager {
    
    public static let shared: TokensDataManager = TokensStorageManager() // создаем Синглтон
    
    private init(){}
    
    private lazy var container: NSPersistentContainer = {
        
        let appDelegate = UIApplication.shared.delegate as? AppDelegate
        
        if let appDelegate = appDelegate {
            return appDelegate.persistentContainer
        }
        
        return NSPersistentContainer()
    }()
    
    private var fetch = NSFetchRequest<TokenObject>(entityName: "TokenObject")

    
    func save(token: Token, completion: @escaping () -> ()) {

        container.performBackgroundTask { (context) in
            
            guard let allTokens = try? context.fetch(self.fetch) else { return }
            
            for tokenInContext in allTokens {
                context.delete(tokenInContext)
            }
            
            let currentTokenObject = NSEntityDescription.insertNewObject(forEntityName: "TokenObject", into: context) as? TokenObject
            
            currentTokenObject?.refresh = token.refresh
            currentTokenObject?.access = token.access
            
            
            try? context.save()
            
            DispatchQueue.main.async {
                completion()
            }
        }
    }
    
    func get() -> Token? {
        
        guard let allTokens = try? container.viewContext.fetch(fetch) else { return nil }
            
        return allTokens.first?.toToken()
    }
    
    func delete(completion: @escaping () -> ()) {
        
        container.performBackgroundTask { (context) in
            
            guard let allTokens = try? context.fetch(self.fetch) else { return }
            
            for tokenInContext in allTokens {
                context.delete(tokenInContext)
            }
            
            try? context.save()
            
            DispatchQueue.main.async {
                completion()
            }
        }
    }
    
    
    
}
