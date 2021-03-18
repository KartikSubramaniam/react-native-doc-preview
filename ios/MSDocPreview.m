#import "MSDocPreview.h"
#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import <MobileCoreServices/MobileCoreServices.h>
#import <AVKit/AVKit.h>
#if __has_include("RCTLog.h")
#import "RCTLog.h"
#else
#import <React/RCTLog.h>
#endif

@implementation MSDocPreview

@synthesize docController;

CGRect screenBounds;
CGFloat screenWidth;
CGFloat screenHeight;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()
/**
 * openDoc
 * open Base64 String
 * Parameters: NSArray
 */
RCT_EXPORT_METHOD(openDoc:(NSArray *)array callback:(RCTResponseSenderBlock)callback)
{
    
    __weak MSDocPreview* weakSelf = self;
    dispatch_queue_t asyncQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(asyncQueue, ^{
        NSDictionary* dict = [array objectAtIndex:0];
        NSString* urlStr = dict[@"url"];
        
        NSURL* tmpFileUrl = [[NSURL alloc] initFileURLWithPath:urlStr];
        weakSelf.fileUrl = tmpFileUrl;
        
        dispatch_async(dispatch_get_main_queue(), ^{
            QLPreviewController* cntr = [[QLPreviewController alloc] init];
            cntr.delegate = weakSelf;
            cntr.dataSource = weakSelf;
            if (callback) {
                callback(@[[NSNull null], array]);
            }
            UIViewController* root = [[[UIApplication sharedApplication] keyWindow] rootViewController];
            [root presentViewController:cntr animated:YES completion:nil];
        });
        
    });
}

RCT_EXPORT_METHOD(shareDoc:(NSArray *)array callback:(RCTResponseSenderBlock)callback)
{
    
    NSDictionary* dict = [array objectAtIndex:0];
    NSString* urlStr = dict[@"url"];
    
    NSRange extRange = [urlStr rangeOfString:@"." options:NSBackwardsSearch];
    NSInteger extPos = (NSInteger)extRange.location + 1;
    NSString *extension = [urlStr substringFromIndex:extPos];
    
    NSURL* tmpFileUrl = [[NSURL alloc] initFileURLWithPath:urlStr];

    screenBounds = [UIScreen mainScreen].bounds;
    screenWidth = CGRectGetWidth(screenBounds)  ;
    screenHeight = CGRectGetHeight(screenBounds) ;
    UIInterfaceOrientation interfaceOrientation = [UIApplication sharedApplication].statusBarOrientation;
    
    if(UIInterfaceOrientationIsPortrait(interfaceOrientation)){
        screenBounds.size = CGSizeMake(screenWidth, screenHeight);
    } else if(UIInterfaceOrientationIsLandscape(interfaceOrientation)){
        screenBounds.size = CGSizeMake(screenHeight, screenWidth);
    }
    
    CGRect rect = CGRectMake(screenWidth, -50, 400, 200);
    NSString *uti = nil;
     uti = (__bridge NSString *)UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension, (__bridge CFStringRef) extension, NULL);
    
    self.docController = [UIDocumentInteractionController interactionControllerWithURL: tmpFileUrl];
    self.docController.delegate = self;
    self.docController.UTI = uti;
    
    UIViewController *ctrl = [[[[UIApplication sharedApplication] delegate] window] rootViewController];

    if (![self.docController presentOpenInMenuFromRect:rect inView:ctrl.view animated:YES]) {
        UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:nil message:@"There are no installed apps that can open this file." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alertView show];
        NSLog(@"No App to Support Document");
    }
}


- (NSInteger) numberOfPreviewItemsInPreviewController: (QLPreviewController *) controller
{
    return 1;
}

- (id <QLPreviewItem>) previewController: (QLPreviewController *) controller previewItemAtIndex: (NSInteger) index
{
    return self;
}

#pragma mark - QLPreviewItem protocol

- (NSURL*)previewItemURL
{
    return self.fileUrl;
}

- (UIViewController *)documentInteractionControllerViewControllerForPreview:(UIDocumentInteractionController *)controller {
    return self;
}

@end
