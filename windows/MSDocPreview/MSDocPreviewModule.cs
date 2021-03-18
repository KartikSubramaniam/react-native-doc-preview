using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace MS.MSDocPreview
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class MSDocPreviewModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="MSDocPreviewModule"/>.
        /// </summary>
        internal MSDocPreviewModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "MSDocPreview";
            }
        }
    }
}
