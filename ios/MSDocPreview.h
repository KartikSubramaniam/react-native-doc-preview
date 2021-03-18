
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import <Foundation/Foundation.h>
#import <QuickLook/QuickLook.h>

@interface MSDocPreview : NSObject <RCTBridgeModule, QLPreviewControllerDelegate, QLPreviewControllerDataSource, QLPreviewItem, UIDocumentInteractionControllerDelegate>
@property (nonatomic, retain) UIDocumentInteractionController *docController;
@property (strong, nonatomic) NSURL* fileUrl;
@property (strong, nonatomic) NSURL* fileName;

@property (readonly) NSURL* previewItemURL;
@end
